package com.pongift20.marketplace.backend.user.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pongift20.marketplace.backend.utils.AES256Util;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.Alias;
import org.springframework.util.StringUtils;

@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("user")
public class UserDto {
    private String userUid;
    private String userAlias;
    private String userName;
    @Setter
    private String userPhoneNumber;
    @JsonIgnore
    private String loginPassword;
    private boolean enabled;

    @JsonIgnore
    public String getDecodeUserPhoneNumber() {
        if (!StringUtils.hasText(userPhoneNumber)) return "";
        String phoneNumber;
        try {
            AES256Util aes256Util = new AES256Util("UTF-8");
            phoneNumber = aes256Util.aesDecode(userPhoneNumber);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }

        return phoneNumber;
    }
}
