package com.pongift20.marketplace.backend.giftcard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("giftCard")
public class GiftCardDto {
    private long giftWalletSeq; // 상품권 PK
    private String goodsName; // 상품명
    private String goodsThumbnailSrc;
    private boolean giftNoUsed;
    private String giftExpiryDate; // 상품권 유효기간
    private String giftUrl;
    private String storeName;
    private String serviceName;
}
