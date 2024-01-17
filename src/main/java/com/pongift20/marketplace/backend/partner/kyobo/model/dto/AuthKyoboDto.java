package com.pongift20.marketplace.backend.partner.kyobo.model.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Alias("authKyobo")
public class AuthKyoboDto {
    private String userSub;
    private String userUid;
    private String userName;
    private String userContact;
    private String accountProvider;
    private String alias;
    private Date createAt;
    private Date modifyAt;
}
