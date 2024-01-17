package com.pongift20.marketplace.backend.order.model.vo;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
public class StandByOrderResponseVo {
    private Order order;
    private Goods goods;

    @Getter
    @Builder
    public static class Order {
        private String orderNo;
        private int orderCount;
        private BigDecimal orderPaymentAmount;
        private Date createdAt;
    }

    @Getter
    @Builder
    public static class Goods {
        private String goodsName;
        private String storeName;
        private BigDecimal goodsSalePrice;
        private BigDecimal goodsRetailPrice;
    }
}
