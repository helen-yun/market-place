package com.pongift20.marketplace.backend.internal.giftcard.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GiftCardExtensionDto {
    private String tradeId;
    private String productOrderId;
    private String ledgerId;
    private String expiryDate;
    private String giftNo;
}
