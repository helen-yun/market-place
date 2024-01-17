package com.pongift20.marketplace.backend.order.model.dto;

import com.pongift20.marketplace.backend.order.code.OrderCode;
import com.pongift20.marketplace.backend.order.model.vo.StandByOrderRequestVo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class StandByOrderRequestVoTest {

    /**
     * 결제금액 리턴
     */
    @Test
    void getPaymentAmount() {
        // GIVEN
        BigDecimal salePrice = new BigDecimal("3000");

        StandByOrderRequestVo buyOrder = StandByOrderRequestVo.builder()
                .orderCount(3)
                .buyType(OrderCode.BuyType.BUY)
                .build();

        List<StandByOrderRequestVo.Receiver> receiverList = new ArrayList<>();
        receiverList.add(new StandByOrderRequestVo.Receiver());
        receiverList.add(new StandByOrderRequestVo.Receiver());
        receiverList.add(new StandByOrderRequestVo.Receiver());
        StandByOrderRequestVo giftOrder = StandByOrderRequestVo.builder()
                .buyType(OrderCode.BuyType.GIFT)
                .receiverList(receiverList)
                .build();

        BigDecimal resultPaymentAmount = new BigDecimal("9000");

        // WHEN
        BigDecimal buyPaymentAmount = buyOrder.getPaymentAmount(salePrice, buyOrder.getOrderCount());
        BigDecimal giftPaymentAmount = buyOrder.getPaymentAmount(salePrice, giftOrder.getOrderCount());

        // THEN
        Assertions.assertThat(buyPaymentAmount).isEqualByComparingTo(resultPaymentAmount);
        Assertions.assertThat(giftPaymentAmount).isEqualByComparingTo(resultPaymentAmount);
    }

    /**
     * 타입별 주문갯수 리턴
     */
    @Test
    void getOrderCount() {
        // GIVEN
        StandByOrderRequestVo buyOrder = StandByOrderRequestVo.builder()
                .orderCount(3)
                .buyType(OrderCode.BuyType.BUY)
                .build();

        List<StandByOrderRequestVo.Receiver> receiverList = new ArrayList<>();
        receiverList.add(new StandByOrderRequestVo.Receiver());
        receiverList.add(new StandByOrderRequestVo.Receiver());
        receiverList.add(new StandByOrderRequestVo.Receiver());
        StandByOrderRequestVo giftOrder = StandByOrderRequestVo.builder()
                .buyType(OrderCode.BuyType.GIFT)
                .receiverList(receiverList)
                .build();

        // WHEN
        int buyCount = buyOrder.getOrderCount();
        int giftCount = giftOrder.getOrderCount();

        // THEN
        Assertions.assertThat(buyCount).isEqualTo(3);
        Assertions.assertThat(giftCount).isEqualTo(3);
    }
}