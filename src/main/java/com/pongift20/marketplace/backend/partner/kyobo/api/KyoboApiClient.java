package com.pongift20.marketplace.backend.partner.kyobo.api;

import com.pongift20.marketplace.backend.partner.kyobo.req.KyoboApiRequest;
import com.pongift20.marketplace.backend.partner.kyobo.res.KyoboApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class KyoboApiClient {
    private final WebClient webClient;

    @Value("${kyobo.api-host}")
    private String KYOBO_HOST;

    public KyoboApiClient() {
        this.webClient = WebClient.create();
    }

    /**
     * 토큰 검증 요청
     */
    public KyoboApiResponse tokenVerification(KyoboApiRequest kyoboRequest){
        String apiUrl = KYOBO_HOST + "/v1/api/platfos/auth";

        return webClient.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(kyoboRequest), KyoboApiRequest.class)
                .retrieve()
                .bodyToMono(KyoboApiResponse.class)
                .block();
    }
}
