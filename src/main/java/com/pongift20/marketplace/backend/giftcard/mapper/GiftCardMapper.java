package com.pongift20.marketplace.backend.giftcard.mapper;

import com.pongift20.marketplace.backend.giftcard.model.dto.GiftCardDto;
import com.pongift20.marketplace.backend.giftcard.model.dto.GiftWalletDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GiftCardMapper {

    List<GiftWalletDto> selectGiftWallet();

    /**
     * 상품권 목록 조회
     *
     * @param userUid    User Uid
     * @param giftNoUsed 사용 여부
     * @param lastSeq    마지막 PK (페이징)
     * @param pageSize   페이지 사이즈
     * @return
     */
    List<GiftCardDto> selectGiftCardList(String userUid, boolean giftNoUsed, long lastSeq, int pageSize);

    /**
     * 상품권 갯수 카운트
     *
     * @param userUid    UserUid
     * @param giftNoUsed 사용 여부
     * @return
     */
    long countGiftWalletList(String userUid, boolean giftNoUsed);

    /**
     * 상품권 단건 조회
     * @param userUid       UserUid
     * @param giftWalletSeq PK
     * @author sjeg
     * @return {GiftCardDto}
     */
    GiftCardDto selectGiftCardDetail(String userUid, long giftWalletSeq);

    /**
     * 상품권 단건 삽입
     *
     * @param giftWalletDto 상품권 데이터
     * @return
     */
    long insertGiftWallet(GiftWalletDto giftWalletDto);

    /**
     * 상품권 다건 삽입
     *
     * @param giftWalletDtoList 상품권 데이터 리스트
     * @return
     */
    long insertGiftWalletList(List<GiftWalletDto> giftWalletDtoList);

    long updateGiftWalletUsed(String giftNo, boolean giftNoUsed);

    long extendGiftCardExpirationDate(String giftNo, String expiryDate);

}
