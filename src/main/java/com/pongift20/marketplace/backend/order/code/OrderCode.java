package com.pongift20.marketplace.backend.order.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface OrderCode {
    @Getter
    @AllArgsConstructor
    enum State {
        STAND_BY("standBy", "주문 대기"),
        PURCHASE("purchase", "결제 완료"),
        RETURN("return", "반품 처리");

        private final String code;
        private final String alias;
    }

    @Getter
    @AllArgsConstructor
    enum BuyType {
        GIFT("gift", "선물하기"),
        BUY("buy", "일반구매");

        private final String code;
        private final String alias;

        public boolean equals(BuyType buyType) {
            return buyType.getCode().equals(this.getCode());
        }
    }
}
