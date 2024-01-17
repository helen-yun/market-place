package com.pongift20.marketplace.backend.order.model.vo;

import lombok.*;

import java.util.List;

/**
 * 2.0 상품권 발행 요청 Vo
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCardRequestVo {
    private String channelGb;
    private String tradeId;
    private String tradeDate;
    private Buyer buyer;
    private Receiver receiver;
    private List<MultiTrade> multiTradeList;
    private Goods goods;

    @Getter
    @Builder
    public static class Buyer {
        private String name;
        private String phoneNo;
    }

    @Getter
    @Builder
    public static class Receiver {
        private String name;
        private String phoneNo;
    }

    @Getter
    @Builder
    public static class Goods {
        @Setter
        private String productOrderId;
        private String goodsId;
        private String goodsNm;
        private int saleCnt;
        private int salePrice;
    }

    @Getter
    @Builder
    public static class MultiTrade {
        private Receiver receiver;
        private String productOrderId;
    }
}

