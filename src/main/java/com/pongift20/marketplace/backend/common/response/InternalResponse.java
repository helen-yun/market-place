package com.pongift20.marketplace.backend.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data

public class InternalResponse {
    private Response response;
    private Object data;
    private Object list;

    @Data

    public static class Response {
        private boolean success;
        private String code;
        private String message;

        public Response(boolean success, String code, String message) {
            this.success = success;
            this.code = code;
            this.message = message;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum ResultCode {
        OK("0001");
        private final String code;
    }
}