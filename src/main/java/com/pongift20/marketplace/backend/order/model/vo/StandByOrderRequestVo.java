package com.pongift20.marketplace.backend.order.model.vo;

import com.pongift20.marketplace.backend.order.code.OrderCode;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class StandByOrderRequestVo {
    @Min(1)
    private long goodsSeq;
    @NotNull
    private OrderCode.BuyType buyType;
    @Valid
    private List<Receiver> receiverList;
    private int orderCount;
    private String orderMessage; // 상품권 메세지

    @Data
    public static class Receiver {
        private String receiverName;
        @NotNull
        @NotEmpty
        private String receiverPhoneNumber;
    }

    public BigDecimal getPaymentAmount(BigDecimal salePrice, int orderCount) {
        BigDecimal orderCountBigDecimal = BigDecimal.valueOf(orderCount);
        return salePrice.multiply(orderCountBigDecimal);
    }

    /**
     * 주문 갯수 return
     *
     * @return orderCount
     */
    public int getOrderCount() {
        if (OrderCode.BuyType.BUY.equals(buyType)) return orderCount; // 일반 구매인 경우 파라메터에 포함
        if (receiverList == null) return 0;
        return receiverList.size(); // 선물하기인 경우 수신자 수 만큼 return
    }
}
