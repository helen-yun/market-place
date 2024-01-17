package com.pongift20.marketplace.backend.goods.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pongift20.marketplace.backend.goods.model.dto.GoodsDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GoodsVo {
    private Meta meta;
    private List<GoodsDto> list;
    private GoodsDto detail;

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public static class Meta {
        private long totalCount;
        private String nextToken;
    }
}
