package com.pongift20.marketplace.backend.security.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChannelApiInfoMapper {
    /**
     * apiKey, apiSecret, accessIp 기준 정보 조회
     *
     * @param apiKey
     * @param apiSecret
     * @param accessIp
     * @return
     */
    Long selectProviderSeqByApiKeyAndSecretAndIp(String apiKey, String apiSecret, String accessIp);

}
