package com.pongift20.marketplace.backend.utils;

import com.pongift20.marketplace.backend.common.AppConfig;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AES256UtilTest {
    private String notEncodeData = "01052846056";
    private String encodeData = "d672051e415f7b29f146ed7f76c60a48535b4ce5d5bd38ed5526ce50c4a4f4a6";

    /**
     * 인코딩 테스트
     * @throws Exception
     */
    @Test
    void aesEncode() throws Exception{
        AES256Util test = new AES256Util("UTF-8");
        String data = test.aesEncode(notEncodeData);
        System.out.println(data);
    }

    /**
     * 디코딩 테스트
     * @throws Exception
     */
    @Test
    void aesDecode() throws Exception{
        AES256Util test = new AES256Util("UTF-8");
        String data = test.aesDecode(encodeData);
        System.out.println(data);
    }

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("iso date")
    void isoDateTime1(){
       /* 기존
       OffsetDateTime dateTime;

        // 현재 시간 정보 가져오기
        LocalDateTime now = LocalDateTime.now();
        ZoneOffset offset = ZoneOffset.ofHours(9); // 예시로 +09:00 오프셋을 사용

        dateTime = now.atOffset(offset);*/


        OffsetDateTime dateTime;
        String isoDateTime;

        // 현재 시간 정보 가져오기
        LocalDateTime now = LocalDateTime.now();
        ZoneOffset offset = ZoneOffset.ofHours(9); // 예시로 +09:00 오프셋을 사용

        dateTime = now.atOffset(offset);
        isoDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));

        System.out.println(isoDateTime);
    }

    @Test
    void aesEncode2() {
        // 여기에 사용하는 AES256Util 클래스의 생성자가 UTF-8을 지원하는지 확인해야 합니다.
        String plainPassword = "platfos0914!@"; // 여기에 비밀번호를 입력하세요

        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
        System.out.println("Hashed Password: " + hashedPassword);
    }
}