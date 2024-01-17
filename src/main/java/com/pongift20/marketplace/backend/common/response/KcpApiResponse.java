package com.pongift20.marketplace.backend.common.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * KCP API 결제 서버 응답 형식
 */
@Data
public class KcpApiResponse {
    @JsonProperty("res_cd")
    private String resCd;
    @JsonProperty("res_msg")
    private String resMsg;
}
