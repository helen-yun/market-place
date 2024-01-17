package com.pongift20.marketplace.backend.order.model.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("orderStandBy")
public class OrderStandByDto {
    private long orderStandBySeq;
    private String userUid;
    private long goodsSeq;
    private long storeSeq;
    private String orderNo;
    private String orderBuyType;
    private BigDecimal orderPaymentAmount;
    private int orderCount;
    private String orderMessage;
    private String storeName;
    private String goodsName;
    private BigDecimal goodsSalePrice;
    private BigDecimal goodsRetailPrice;
    private Date goodsModifiedAt;
    private Date createdAt;
}
