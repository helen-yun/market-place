package com.pongift20.marketplace.backend.order.model.vo;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Builder
public class PurchaseOrderRequestVo {
    @NotNull
    private String purchaseCode; // orderNo
    @NotNull
    private String tno; // transactNo 주문번호 (KCP)
    @NotNull
    private String purchaseAuthorizationCode; // 결제 승인번호 (KCP)
    @NotNull
    private BigDecimal price; // 결제 금액
}
