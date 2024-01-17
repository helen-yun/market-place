package com.pongift20.marketplace.backend.partner.kyobo.model.vo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthKyoboVo {
    private String userSub;
    private String userUid;
    private String userName;
    private String userContact;
    private String createAt;
    private String modifyAt;
}
