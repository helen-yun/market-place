package com.pongift20.marketplace.backend.giftcard.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonSerialize
public class PongiftSendGiftCardResponseVo {
    private String giftNo; // 상품권 번호
    private String detailNo; // 승인번호 * tradeNo
    private String expiryDate; // 유효기간
    private String publicCode; // 상품권 공용 코드
}
