package com.pongift20.marketplace.backend.order.model.dto;

import com.pongift20.marketplace.backend.order.model.dto.TradeHistoryDto;
import com.pongift20.marketplace.backend.utils.AES256Util;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("tradeStandBy")
public class TradeStandByDto {
    private long tradeStandBySeq;
    private long orderStandBySeq;
    private String tradeNo;
    private String tradeReceiverName;
    @Setter
    private String tradeReceiverPhoneNumber;
    private Date createdAt;

    public TradeHistoryDto toTradeHistoryVo(long orderHistorySeq) {
        return TradeHistoryDto.builder()
                .orderHistorySeq(orderHistorySeq)
                .tradeNo(tradeNo)
//                .tradeReceiverName(tradeReceiverName) //TODO: 수신자명 넣을지..?
                .tradeReceiverPhoneNumber(tradeReceiverPhoneNumber)
                .build();
    }

    public String getDecodePhoneNumber() throws RuntimeException {
        try {
            AES256Util aes256Util = new AES256Util("UTF-8");
            return aes256Util.aesDecode(tradeReceiverPhoneNumber);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }
}
