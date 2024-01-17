package com.pongift20.marketplace.backend.common.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 폰기프트 2.0 API 응답 형식
 */
@Data
public class PongiftApiResponse {
    private Response response;
    private Object data;
    private Object list;

    @Data
    public static class Response {
        private String serviceId;
        private String code;
        private String message;
    }

    @Getter
    @AllArgsConstructor
    public enum ResponseCode {
        OK("0001");
        private final String code;
    }
}
