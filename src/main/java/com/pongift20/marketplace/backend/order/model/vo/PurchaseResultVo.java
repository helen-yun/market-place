package com.pongift20.marketplace.backend.order.model.vo;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 주문 완료 건 조회 vo
 */
@Getter
@Builder
public class PurchaseResultVo {
    private BigDecimal paymentAmount;
    private String buyerName;
    private String goodsName;
    private int orderCount;
    private String storeName;
}
