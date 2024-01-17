package com.pongift20.marketplace.backend.common.response;

import com.pongift20.marketplace.backend.common.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ResponseCode errorCode;
}