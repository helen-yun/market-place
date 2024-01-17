package com.pongift20.marketplace.backend.common.response;

import com.pongift20.marketplace.backend.common.code.ResponseCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class CustomErrorResponse {
    private final int status;
    private final String code;
    private final String message;

    public static ResponseEntity<CustomErrorResponse> toResponse(ResponseCode code){
        return ResponseEntity
                .status(code.getStatus())
                .body(CustomErrorResponse.builder()
                        .status(code.getStatus())
                        .code(code.getCode())
                        .message(code.getMessage())
                        .build());
    }
}
