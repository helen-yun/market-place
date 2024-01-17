package com.pongift20.marketplace.backend.internal.api;

import com.pongift20.marketplace.backend.common.response.KcpApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class KcpApiClient {
    private final WebClient webClient;

    @Value("${pongift.pay-host}")
    private String PAY_SERVER_HOST;

    public KcpApiClient() {
        this.webClient = WebClient.create();
    }

    public KcpApiResponse requestCancelOrder(String tno) {
        String apiUrl = PAY_SERVER_HOST + "cancel.php";
        log.info(apiUrl);
        return webClient.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("tno", tno))
                .exchangeToMono(response -> {
                    if (response.statusCode().isError()) {
                        return response.bodyToMono(KcpApiResponse.class)
                                .flatMap(errorBody -> {
                                    log.error("Error resCd: " + errorBody.getResCd());
                                    log.error("Error resMsg: " + errorBody.getResMsg());
                                    return Mono.just(errorBody);
                                });
                    } else {
                        return response.bodyToMono(KcpApiResponse.class);
                    }
                })
                .block();
    }
}