package com.pongift20.marketplace.backend.partner.kyobo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pongift20.marketplace.backend.partner.kyobo.model.dto.AuthKyoboDto;
import com.pongift20.marketplace.backend.partner.kyobo.model.vo.AuthKyoboVo;
import com.pongift20.marketplace.backend.partner.kyobo.res.KyoboApiResponse;
import com.pongift20.marketplace.backend.partner.kyobo.service.AuthKyoboService;
import com.pongift20.marketplace.backend.user.model.dto.UserDto;
import com.pongift20.marketplace.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth/kyobo")
public class AuthKyoboController {
    private final AuthKyoboService authKyoboService;
    private final UserService userService;

    @Value("${pongift.main-page}")
    private String MAIN_PAGE_URL;

    @Value("${pongift.error-page}")
    private String ERROR_PAGE_URL;

    @Value("${pongift.terms-page}")
    private String TERMS_PAGE_URL;

    /**
     * 교보 로그인
     * @param token 교보 토큰
     * @param request
     * @return
     */
    @GetMapping("")
    public ResponseEntity<Object> certificationKyobo(@RequestParam("t") String token, HttpServletRequest request) {
        HttpSession session = request.getSession();

        log.info("교보 로그인 요청 >>> token: {}", token);
        if(StringUtils.isEmpty(token)) {
            return setRedirectPage(ERROR_PAGE_URL);
        }

        //교보 토큰 검증
        KyoboApiResponse kyoboApiResponse = authKyoboService.certificationKyobo(token);
        if(kyoboApiResponse.getCode() != HttpStatus.OK.value()) {
            return setRedirectPage(ERROR_PAGE_URL);
        }

        //유저 데이터 디코딩
        AuthKyoboVo authKyoboVo = authKyoboService.decryptedUserInfo(kyoboApiResponse, token);
        if(authKyoboVo == null) {
            return setRedirectPage(ERROR_PAGE_URL);
        }

        //가입/미가입 유저 체크
        AuthKyoboDto authKyoboDto = authKyoboService.selectKyoboUser(authKyoboVo.getUserSub());
        //정보 조회 안될 경우 세션에 회원 토큰 올려놓기
        if(authKyoboDto == null){
            session.setAttribute("kyoboToken", token);
            session.setAttribute("kyoboData", kyoboApiResponse);
            return setRedirectPage(TERMS_PAGE_URL);
        }

        //기존 정보와 다를 경우 업데이트 처리
        if(!authKyoboVo.getUserName().equals(authKyoboDto.getUserName()) ||
            !authKyoboVo.getUserContact().equals(authKyoboDto.getUserContact())){
            authKyoboService.updateKyoboUser(authKyoboVo, authKyoboDto.getUserUid());
        }

        //조회 될 경우 세션 올리기
        UserDto userDto = userService.selectUserByUserUid(authKyoboDto.getUserUid());
        authKyoboService.updateAuthentication(userDto);

        return setRedirectPage(MAIN_PAGE_URL);
    }

    /**
     * 교보 약관 동의 -> 회원가입
     * @param request
     * @return
     */
    @PostMapping("/join")
    public ResponseEntity<Object> certificationKyoboAgreeTerms(HttpServletRequest request) {
        HttpSession session = request.getSession();

        String kyoboToken = (String) session.getAttribute("kyoboToken");
        Object kyoboData = session.getAttribute("kyoboData");

        log.info("교보 회원가입 요청 >>> token: {}", kyoboToken);
        if(StringUtils.isEmpty(kyoboToken)) {
            log.error("교보 회원 토큰 세션 만료 error");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if(kyoboData == null){
            log.error("교보 회원 데이터 세션 만료 error");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        //유저 데이터 디코딩
        KyoboApiResponse kyoboApiResponse = (KyoboApiResponse) kyoboData;
        AuthKyoboVo authKyoboVo = authKyoboService.decryptedUserInfo(kyoboApiResponse, kyoboToken);

        //회원 등록 처리
        AuthKyoboDto authKyoboDto = authKyoboService.insertKyoboUser(authKyoboVo);

        //유저 정보 세션 저장
        UserDto userDto = userService.selectUserByUserUid(authKyoboDto.getUserUid());
        authKyoboService.updateAuthentication(userDto);

        return setRedirectPage(MAIN_PAGE_URL);
    }

    /**
     * 리다이렉트 페이지 셋팅
     * @param url 리다이렉트 요청할 url
     * @return
     * @throws URISyntaxException
     */
    private ResponseEntity<Object> setRedirectPage(String url) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try{
            httpHeaders.setLocation(new URI(url));
        }catch (URISyntaxException e){
            log.error("redirect set error: {}", e.getMessage());
        }
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
}

