package com.pongift20.marketplace.backend.internal.giftcard.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * 2.0 상품권 사용 처리 요청 객체
 */
@Getter
@Builder
public class GiftCardDto {
    private String tradeId;
    private String ledgerId;
    private String productOrderId; // 상품주문번호
    private String giftNo; // 상품권 번호
}
