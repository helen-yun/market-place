package com.pongift20.marketplace.backend.order.api;

import com.pongift20.marketplace.backend.common.response.PongiftApiResponse;
import com.pongift20.marketplace.backend.order.model.vo.GiftCardRequestVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class GiftCardApiClient {
    private final WebClient webClient;

    @Value("${pongift.api-host}")
    private String PONGIFT_API_HOST;

    public GiftCardApiClient() {
        this.webClient = WebClient.create();
    }

    public PongiftApiResponse requestMultiCreateGiftCard(GiftCardRequestVo giftCardRequestVo) {
        String apiUrl = PONGIFT_API_HOST + "trade/approved/request/multi";

        return webClient.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(giftCardRequestVo))
                .retrieve()
                .bodyToMono(PongiftApiResponse.class)
                .block();
    }

    public PongiftApiResponse requestCreateGiftCard(GiftCardRequestVo giftCardRequestVo) {
        String apiUrl = PONGIFT_API_HOST + "trade/approved/request";

        return webClient.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(giftCardRequestVo))
                .retrieve()
                .bodyToMono(PongiftApiResponse.class)
                .block();
    }
}