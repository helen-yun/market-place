package com.pongift20.marketplace.backend.giftcard.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pongift20.marketplace.backend.giftcard.model.dto.GiftCardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftWalletVo {
    Meta meta;
    List<GiftCardDto> list;
    GiftCardDto detail;

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public static class Meta {
        private long totalCount;
        private long nextToken;
    }
}
