package com.pongift20.marketplace.backend.common;

import com.pongift20.marketplace.backend.internal.api.KcpApiClient;
import com.pongift20.marketplace.backend.order.api.GiftCardApiClient;
import com.pongift20.marketplace.backend.partner.kyobo.api.KyoboApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public GiftCardApiClient giftCardApiClient() {
        return new GiftCardApiClient();
    }

    @Bean
    public KcpApiClient kcpApiClient() {
        return new KcpApiClient();
    }

    @Bean
    public KyoboApiClient kyoboApiClient(){
        return new KyoboApiClient();
    }
}