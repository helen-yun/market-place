package com.pongift20.marketplace.backend.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private final int status;
    private final long timestamp;
    private final String domain;
    private final String uid;
}
