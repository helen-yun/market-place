package com.pongift20.marketplace.backend.order.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pongift20.marketplace.backend.order.model.dto.OrderHistoryDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderVo {
    private Meta meta;
    private List<OrderHistoryDto> list;
    private OrderHistoryDto order;

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public static class Meta {
        private String buyerName;
        private long nextToken;
    }

}
