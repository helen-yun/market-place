package com.pongift20.marketplace.backend.partner.kyobo.service;

import com.pongift20.marketplace.backend.partner.kyobo.api.KyoboApiClient;
import com.pongift20.marketplace.backend.partner.kyobo.mapper.AuthKyoboMapper;
import com.pongift20.marketplace.backend.partner.kyobo.model.dto.AuthKyoboDto;
import com.pongift20.marketplace.backend.partner.kyobo.model.vo.AuthKyoboVo;
import com.pongift20.marketplace.backend.partner.kyobo.req.KyoboApiRequest;
import com.pongift20.marketplace.backend.partner.kyobo.res.KyoboApiResponse;
import com.pongift20.marketplace.backend.user.mapper.UserMapper;
import com.pongift20.marketplace.backend.user.model.dto.BaseDto;
import com.pongift20.marketplace.backend.user.model.dto.UserDto;
import com.pongift20.marketplace.backend.user.service.UserService;
import com.pongift20.marketplace.backend.utils.AES256GCMUtil;
import com.pongift20.marketplace.backend.utils.AES256Util;
import com.pongift20.marketplace.backend.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthKyoboService {
    private final KyoboApiClient kyoboApiClient;
    private final AuthKyoboMapper authKyoboMapper;
    private final UserService userService;

    @Value("${kyobo.iv}")
    private String KYOBO_IV;

    //TODO: 변경해야함
    @Value("${kyobo.url}")
    private String KYOBO_URL;


    /**
     * 교보 토큰 검증
     * @param token 교보 토큰
     * @return
     */
    public KyoboApiResponse certificationKyobo(String token){
        try{
            //토큰 검증 로직
            KyoboApiRequest kyoboRequest = new KyoboApiRequest();
            kyoboRequest.setToken(token);
            KyoboApiResponse kyoboApiResponse = kyoboApiClient.tokenVerification(kyoboRequest);
            log.info("교보 토큰 검증: {}", JsonUtils.toJson(kyoboApiResponse));
            if(kyoboApiResponse == null) {
                log.error("교보 토큰 검증 >> {}", "kyoboApiResponse null");
                return null;
            }
            return kyoboApiResponse;
        }catch (Exception e){
            log.error("### 교보 토큰 검증 실패 : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 유저 정보 디코딩
     * @param kyoboApiResponse 유저 정보
     * @return
     */
    public AuthKyoboVo decryptedUserInfo(KyoboApiResponse kyoboApiResponse, String token) {
        log.info("교보 유저 정보:{}", kyoboApiResponse.getBody());
        byte[] decrypted;

        try {
            decrypted = AES256GCMUtil.decrypt(Base64.getUrlDecoder().decode(kyoboApiResponse.getBody().getData()),
                    token.getBytes(StandardCharsets.UTF_8), KYOBO_IV.getBytes(StandardCharsets.UTF_8));

            //개인정보는 한글이 포함될 수 있으므로 utf-8 인코딩을 지정
            AES256Util AES256Util = new AES256Util("UTF-8");
            String decryptedStr = new String(decrypted, StandardCharsets.UTF_8);
            String[] split = decryptedStr.split("\\|");

            if (split.length >= 3) {
                    return AuthKyoboVo.builder()
                        .userSub(split[0]) // CI, 사용자 식별값
                        .userContact(AES256Util.aesEncode(split[1])) // 상품권을 수신할 수 있는 교보회원 연락처
                        .userName(split[2]) // 교보회원이름
                        .build();
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException |
                 ArrayIndexOutOfBoundsException | UnsupportedEncodingException e) {
            log.error("### 교보 회원정보 복호화 실패 : {}", e.getMessage());
            return null;
        }
        return null;
    }

    /**
     * 가입, 미가입 유저 체크
     * @param userSub 교보 유저 ci
     * @return
     */
    public AuthKyoboDto selectKyoboUser(String userSub){
        return authKyoboMapper.selectAuthKyobo(userSub);
    }

    /**
     * 교보 유저 신규 등록
     * @param authKyoboVo 교보 회원 정보
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public AuthKyoboDto insertKyoboUser(AuthKyoboVo authKyoboVo) {
        log.info("### 교보 회원 등록");
        //교보 회원 정보 set
        AuthKyoboDto authKyoboDto = AuthKyoboDto.builder()
                .userUid(UUID.randomUUID().toString())
                .userSub(authKyoboVo.getUserSub())
                .userContact(authKyoboVo.getUserContact())
                .userName(authKyoboVo.getUserName())
                .accountProvider(KYOBO_URL)
                .alias("교보회원")
                .build();

        //user BaseDto set
        BaseDto baseDto = BaseDto.builder()
                .userUid(authKyoboDto.getUserUid())
                .userPhoneNumber(authKyoboDto.getUserContact())
                .userName(authKyoboDto.getUserName())
                .accountProvider(authKyoboDto.getAccountProvider())
                .alias(authKyoboDto.getAlias())
                .build();

        //회원 등록
        long resultUsers = userService.insertUsers(baseDto);
        long resultProfile = userService.insertUserProfile(baseDto);
        long resultAuth = authKyoboMapper.insertAuthKyobo(authKyoboDto);

        if(resultUsers <= 0 | resultProfile <= 0 | resultAuth <= 0){
            log.error("### 교보 회원 등록 실패: {}", authKyoboDto.getUserUid());
            throw new RuntimeException();
        }
        return authKyoboDto;
    }

    /**
     * 교보 회원 업데이트
     * @param authKyoboVo 교보 회원 정보
     * @param uid 기존 회원 uid
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void updateKyoboUser(AuthKyoboVo authKyoboVo, String uid){
        log.info("### 교보 회원 업데이트");
        //교보 회원 정보 set
        AuthKyoboDto authKyoboDto = AuthKyoboDto.builder()
                .userUid(uid)
                .userSub(authKyoboVo.getUserSub())
                .userContact(authKyoboVo.getUserContact())
                .userName(authKyoboVo.getUserName())
                .build();

        //user BaseDto set
        BaseDto baseDto = BaseDto.builder()
                .userUid(authKyoboDto.getUserUid())
                .userPhoneNumber(authKyoboDto.getUserContact())
                .userName(authKyoboDto.getUserName())
                .build();

        long resultProfile = userService.updateUserProfile(baseDto);
        long resultAuth = authKyoboMapper.updateAuthKyobo(authKyoboDto);

        if(resultAuth <= 0 | resultProfile <= 0){
            log.error("### 교보 회원 정보 업데이트 실패: {}", authKyoboDto.getUserUid());
            throw new RuntimeException();
        }
    }

    /**
     * 세션 올리기
     * @param userDto
     */
    public void updateAuthentication(UserDto userDto) {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDto.getUserUid(), null, roles);
        authenticationToken.setDetails(userDto);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
