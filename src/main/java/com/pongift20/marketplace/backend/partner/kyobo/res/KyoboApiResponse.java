package com.pongift20.marketplace.backend.partner.kyobo.res;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KyoboApiResponse {
    /**
     * 응답코드
     */
    private int code;
    /**
     * 응답메세지
     */
    private String message;
    /**
     * 본문
     */
    private UserData body;

    @Data
    public static class UserData{
        private String data;
    }
}
