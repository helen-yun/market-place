package com.pongift20.marketplace.backend.security.service;

import com.pongift20.marketplace.backend.security.mapper.ChannelApiInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelApiInfoService {
    private final ChannelApiInfoMapper channelApiInfoMapper;

    /**
     * apiKey, apiSecret, accessIp 기준 정보 조회
     *
     * @param apiKey
     * @param apiSecret
     * @param accessIp
     * @return providerSeq
     */
    public Long selectProviderSeqByApiKeyAndSecretAndIp(String apiKey, String apiSecret, String accessIp) {
        return channelApiInfoMapper.selectProviderSeqByApiKeyAndSecretAndIp(apiKey, apiSecret, accessIp);
    }
}
