package com.pongift20.marketplace.backend.security.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Getter
@NoArgsConstructor
@Alias("channelApiInfo")
public class ChannelApiInfoDto {
    private long apiSeq;
    private long providerSeq;
    private String apiKey;
    private String apiSecret;

    private String accessIp;
    private String ipType;
    private boolean allow;
    private String description;
}
