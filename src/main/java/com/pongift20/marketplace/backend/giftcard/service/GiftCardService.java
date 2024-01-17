package com.pongift20.marketplace.backend.giftcard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pongift20.marketplace.backend.common.exception.ValidationException;
import com.pongift20.marketplace.backend.common.response.PongiftApiResponse;
import com.pongift20.marketplace.backend.giftcard.mapper.GiftCardMapper;
import com.pongift20.marketplace.backend.giftcard.model.dto.GiftCardDto;
import com.pongift20.marketplace.backend.giftcard.model.dto.GiftWalletDto;
import com.pongift20.marketplace.backend.goods.model.dto.GoodsDto;
import com.pongift20.marketplace.backend.order.api.GiftCardApiClient;
import com.pongift20.marketplace.backend.order.code.OrderCode;
import com.pongift20.marketplace.backend.order.model.vo.GiftCardRequestVo;
import com.pongift20.marketplace.backend.order.model.dto.OrderHistoryDto;
import com.pongift20.marketplace.backend.order.model.dto.TradeStandByDto;
import com.pongift20.marketplace.backend.user.model.dto.UserDto;
import com.pongift20.marketplace.backend.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class GiftCardService {
    private final GiftCardApiClient giftCardApiClient;
    private final GiftCardMapper giftCardMapper;
    private final String MARKET_CHANNEL_GB = "08";
    private final int PAGE_SIZE = 31;

    @Value("${pongift.barcode-host}")
    private String BARCODE_SERVER_HOST;

    public List<GiftCardDto> selectGiftCardList(String userUid, boolean giftNoUsed, long lastSeq) {
        return giftCardMapper.selectGiftCardList(userUid, giftNoUsed, lastSeq, PAGE_SIZE);
    }

    public long countGiftWallet(String userUid, boolean giftNoUsed) {
        return giftCardMapper.countGiftWalletList(userUid, giftNoUsed);
    }

    public GiftCardDto selectGiftCardDetail(String userUid, long giftWalletSeq){
        return giftCardMapper.selectGiftCardDetail(userUid, giftWalletSeq);
    }

    @Transactional
    public long insertGiftWallet(GiftWalletDto giftWalletDto) {
        return giftCardMapper.insertGiftWallet(giftWalletDto);
    }

    @Transactional
    public long insertGiftWalletList(List<GiftWalletDto> giftWalletDtoList) {
        return giftCardMapper.insertGiftWalletList(giftWalletDtoList);
    }

    /**
     * 2.0 상품권 발행 요청 객체 생성
     *
     * @param orderHistoryDto     주문 데이터
     * @param goodsDto            상품 데이터
     * @param userDto             유저 데이터
     * @param tradeStandByDtoList 거래 데이터 목록
     * @return GiftCardRequestDto
     */
    public GiftCardRequestVo createGiftCardRequest(OrderHistoryDto orderHistoryDto, GoodsDto goodsDto, UserDto userDto, List<TradeStandByDto> tradeStandByDtoList) throws JsonProcessingException {
        GiftCardRequestVo.Buyer buyer = GiftCardRequestVo.Buyer.builder().name(userDto.getUserName()).phoneNo(userDto.getUserPhoneNumber()).build();

        GiftCardRequestVo.Goods goods = GiftCardRequestVo.Goods.builder()
                .goodsId(String.valueOf(orderHistoryDto.getGoodsSeq()))
                .goodsNm(goodsDto.getGoodsName())
                .saleCnt(orderHistoryDto.getOrderCount())
                .salePrice(goodsDto.getGoodsSalePrice().intValue())
                .build();

        GiftCardRequestVo giftCardRequestVo = GiftCardRequestVo.builder()
                .tradeId(orderHistoryDto.getOrderNo())
                .channelGb(MARKET_CHANNEL_GB)
                .tradeDate(new SimpleDateFormat("yyyyMMddHHmmss").format(orderHistoryDto.getCreatedAt()))
                .buyer(buyer)
                .goods(goods)
                .build();

        if (OrderCode.BuyType.BUY.getCode().equals(orderHistoryDto.getOrderBuyType())) {
            GiftCardRequestVo.Receiver receiver = GiftCardRequestVo.Receiver.builder()
                    .name(tradeStandByDtoList.get(0).getTradeReceiverName())
                    .phoneNo(tradeStandByDtoList.get(0).getDecodePhoneNumber())
                    .build();
            giftCardRequestVo.setReceiver(receiver);
            // 거래번호 지정
            goods.setProductOrderId(tradeStandByDtoList.get(0).getTradeNo());
        } else { // GIFT
            List<GiftCardRequestVo.MultiTrade> multiTradeList = tradeStandByDtoList.stream().map(tradeStandByDto -> {
                GiftCardRequestVo.Receiver receiver = GiftCardRequestVo.Receiver.builder()
                        .name(tradeStandByDto.getTradeReceiverName())
                        .phoneNo(tradeStandByDto.getDecodePhoneNumber())
                        .build();
                return GiftCardRequestVo.MultiTrade.builder()
                        .receiver(receiver)
                        .productOrderId(tradeStandByDto.getTradeNo())
                        .build();
            }).collect(Collectors.toList());
            giftCardRequestVo.setMultiTradeList(multiTradeList);
        }
        log.info("상품권 발행 요청 객체 생성 >>> : {}", JsonUtils.toJson(giftCardRequestVo));
        return giftCardRequestVo;
    }

    /**
     * 2.0 상품권 발행 전용 Request 생성
     *
     * @param giftCardRequestVo 상품권 발행 요청 vo
     * @param buyType            구매 유형 (BUY, GIFT)
     * @return PongiftApiResponse 2.0 응답값
     */
    public PongiftApiResponse giftCardCreateRequest(GiftCardRequestVo giftCardRequestVo, String buyType) throws JsonProcessingException {
        PongiftApiResponse pongiftApiResponse = new PongiftApiResponse();
        try{
            if (OrderCode.BuyType.BUY.getCode().equals(buyType)) {
                pongiftApiResponse = giftCardApiClient.requestCreateGiftCard(giftCardRequestVo);
            } else {
                pongiftApiResponse = giftCardApiClient.requestMultiCreateGiftCard(giftCardRequestVo);
            }
        }catch (Exception e){
            log.error("상품권 발행 request 에러 : {}", JsonUtils.toJson(pongiftApiResponse));
            log.error(e.getMessage());
        }
        log.info("상품권 발행 response: {} ", JsonUtils.toJson(pongiftApiResponse));
        return pongiftApiResponse;
    }

    /**
     * 상품권 사용 처리
     *
     * @param giftNo
     */
    @Transactional
    public void useGiftCard(String giftNo) {
        if (!StringUtils.hasText(giftNo)) throw new ValidationException();
        long result = giftCardMapper.updateGiftWalletUsed(giftNo, true);
        if (result <= 0) throw new RuntimeException();
    }

    /**
     * 상품권 사용 처리
     *
     * @param giftNo
     */
    @Transactional
    public void cancelGiftCard(String giftNo) {
        if (!StringUtils.hasText(giftNo)) throw new ValidationException();
        long result = giftCardMapper.updateGiftWalletUsed(giftNo, false);
        if (result <= 0) throw new RuntimeException();
    }

    /**
     * 상품권 유효기간 연장
     *
     * @param giftNo
     */
    @Transactional
    public void extendGiftCardExpirationDate(String giftNo, String expiryDate) {
        if (!StringUtils.hasText(giftNo) || !StringUtils.hasText(expiryDate)) throw new ValidationException();
        long result = giftCardMapper.extendGiftCardExpirationDate(giftNo, expiryDate);
        if (result <= 0) {
            log.error("#### 선물하기로 구매한 상품권");
            log.error("#### giftNo = {}", giftNo);
        }
    }

    /**
     * 2.0 상품권 주소 조합
     *
     * @param publicCode 2.0에서 내려주는 상품권 코드
     * @return 2.0 상품권 주소
     */
    public String getPongift20GiftCardUrl(String publicCode) {
        return BARCODE_SERVER_HOST + publicCode;
    }

    /**
     * 마지막 seq 조회 (페이징)
     *
     * @param giftCardDtoList 내 상품권 목록
     * @return lastSeq
     */
    public long getLastOrderSeq(List<GiftCardDto> giftCardDtoList) {
        if (giftCardDtoList.size() <= PAGE_SIZE - 1) {
            return 0;
        }
        GiftCardDto giftCardDto;
        if (giftCardDtoList.size() == PAGE_SIZE) {
            giftCardDto = giftCardDtoList.get(PAGE_SIZE - 2);
        } else {
            giftCardDto = giftCardDtoList.get(giftCardDtoList.size() - 1);
        }
        return giftCardDto.getGiftWalletSeq();
    }
}
