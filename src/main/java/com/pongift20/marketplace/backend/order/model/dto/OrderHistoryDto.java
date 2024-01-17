package com.pongift20.marketplace.backend.order.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("orderHistory")
public class OrderHistoryDto {
    private long orderHistorySeq;
    private long goodsSeq;
    private long storeSeq;
    private String storeName;
    private String serviceName;
    private String goodsName;
    private BigDecimal goodsRetailPrice;
    private BigDecimal goodsSalePrice;
    private Date goodsModifiedAt;
    private String orderNo;
    private String orderBuyType;
    private BigDecimal orderPaymentAmount;
    private String orderTransactNo;
    private String orderApproveNo;
    @Setter
    private String orderState;
    private int orderCount;
    private boolean orderSendStatus;
    private String orderMessage;
    private Date createdAt;
    private Date modifiedAt;
    private Date returnedAt;

    @JsonIgnore
    private String userUid;
}
