package com.pongift20.marketplace.backend.order.model.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("tradeHistory")
public class TradeHistoryDto {
    private long tradeHistorySeq;
    private long orderHistorySeq;
    private String tradeNo;
    private String tradeReceiverName;
    @Setter
    private String tradeReceiverPhoneNumber;
    private Date createdAt;
}
