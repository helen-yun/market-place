package com.pongift20.marketplace.backend.user.model.dto;

import lombok.*;

/**
 * user 관련 테이블 공통 dto
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseDto {
    private String userUid;
    private String userName;
    private String userPhoneNumber;
    private String accountProvider;
    private String alias;
}
