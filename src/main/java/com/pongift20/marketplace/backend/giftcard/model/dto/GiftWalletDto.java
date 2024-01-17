package com.pongift20.marketplace.backend.giftcard.model.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("giftWallet")
public class GiftWalletDto {
    private String giftNo; // 상품권 번호
    private String userUid; // 유저 UID
    private long goodsSeq; // 상품 PK
    private String goodsName; // 상품명
    private String giftUrl; // 상품권 주소
    private int giftNoUsed; // 상품권 사용 여부
    private String giftExpiryDate; // 상품권 유효기간
    private String giftType; //선물 타입(buy:일반구매, gift:선물)
    private String orderNo; //주문 번호
    private Date createdAt;
    private Date modifiedAt;
}
