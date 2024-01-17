package com.pongift20.marketplace.backend.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pongift20.marketplace.backend.common.exception.ValidationException;
import com.pongift20.marketplace.backend.common.response.PongiftApiResponse;
import com.pongift20.marketplace.backend.giftcard.service.GiftCardService;
import com.pongift20.marketplace.backend.goods.model.dto.GoodsDto;
import com.pongift20.marketplace.backend.order.model.vo.GiftCardRequestVo;
import com.pongift20.marketplace.backend.order.model.vo.StandByOrderRequestVo;
import com.pongift20.marketplace.backend.order.model.dto.OrderHistoryDto;
import com.pongift20.marketplace.backend.order.model.dto.OrderStandByDto;
import com.pongift20.marketplace.backend.order.model.dto.TradeHistoryDto;
import com.pongift20.marketplace.backend.order.model.dto.TradeStandByDto;
import com.pongift20.marketplace.backend.user.model.dto.UserDto;
import com.pongift20.marketplace.backend.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class MainOrderService {
    private final OrderService orderService;
    private final GiftCardService giftCardService;

    /**
     * 나에게 구매 주문 데이터 생성 (주문대기)
     *
     * @param orderStandByDto 주문 데이터 (주문대기)
     * @throws ValidationException   유효성 검사 실패 exception
     * @throws RuntimeException      기타 insert 관련 에러
     * @throws DuplicateKeyException orderNo가 중복된 경우
     */
    @Transactional
    public void createStandByOrder(OrderStandByDto orderStandByDto, UserDto userDto) throws ValidationException, RuntimeException, DuplicateKeyException {
        // 주문 대기 데이터 삽입
        orderService.insertStandByOrder(orderStandByDto);

        // 수신자 목록에 대한 거래 목록 생성
        List<TradeStandByDto> tradeStandByDtoList = new ArrayList<>();
        TradeStandByDto tradeStandByDto = TradeStandByDto.builder()
                .orderStandBySeq(orderStandByDto.getOrderStandBySeq())
                .tradeNo(orderService.createOrderNo())
                .tradeReceiverName(userDto.getUserName()) //TODO: 확인 필요
                .tradeReceiverPhoneNumber(userDto.getUserPhoneNumber().replaceAll("-", ""))
                .createdAt(orderStandByDto.getCreatedAt())
                .build();
        tradeStandByDtoList.add(tradeStandByDto);
        orderService.insertStandByTradeList(tradeStandByDtoList);
    }

    /**
     * 선물하기 주문 데이터 생성 (주문대기)
     *
     * @param orderStandByDto 주문 데이터 (주문대기)
     * @param receiverList   수신자 목록
     * @throws ValidationException   유효성 검사 실패 exception
     * @throws RuntimeException      기타 insert 관련 에러
     * @throws DuplicateKeyException orderNo가 중복된 경우
     */
    @Transactional
    public void createGiftStandByOrder(OrderStandByDto orderStandByDto, List<StandByOrderRequestVo.Receiver> receiverList) throws ValidationException, RuntimeException, DuplicateKeyException {
        log.info("### 주문 대기 데이터 생성");
        // 주문 대기 데이터 삽입
        orderService.insertStandByOrder(orderStandByDto);

        // 수신자 목록에 대한 거래 목록 생성
        List<TradeStandByDto> tradeStandByDtoList = new ArrayList<>();
        for (StandByOrderRequestVo.Receiver receiver : receiverList) {
            TradeStandByDto tradeStandByDto = TradeStandByDto.builder()
                    .orderStandBySeq(orderStandByDto.getOrderStandBySeq())
                    .tradeNo(orderService.createOrderNo())
                    .tradeReceiverName("제휴몰" + receiver.getReceiverPhoneNumber().substring(receiver.getReceiverPhoneNumber().length() - 4)) //TODO: 확인 필요
                    .tradeReceiverPhoneNumber(receiver.getReceiverPhoneNumber().replaceAll("-", ""))
                    .createdAt(orderStandByDto.getCreatedAt())
                    .build();
            tradeStandByDtoList.add(tradeStandByDto);
        }
        orderService.insertStandByTradeList(tradeStandByDtoList);
    }

    /**
     * 결제 완료 처리 및 상품권 발송 요청
     *
     * @param orderHistoryDto KCP tno가 포함된 주문 객체
     * @throws ValidationException 유효성 검사 실패 exception
     * @throws RuntimeException    기타 관련 에러
     */
    @Transactional
    public PongiftApiResponse insertPurchaseAndRequestGiftCard(OrderHistoryDto orderHistoryDto, long orderStandBySeq, GoodsDto goodsDto, UserDto userDto) throws ValidationException, RuntimeException, JsonProcessingException {
        log.info("### 결제 완료 처리 및 상품권 발송 요청");
        orderService.insertOrderHistory(orderHistoryDto);
        List<TradeStandByDto> tradeStandByDtoList = orderService.selectTradeStandByListByOrderStandBySeq(orderStandBySeq);

        List<TradeHistoryDto> tradeHistoryDtoList = tradeStandByDtoList.stream()
                .map(tradeStandByDto -> tradeStandByDto.toTradeHistoryVo(orderHistoryDto.getOrderHistorySeq()))
                .collect(Collectors.toList());

        orderService.insertTradeHistoryList(tradeHistoryDtoList);

        // 2.0 상품권 발행 API 요청 객체 생성
        GiftCardRequestVo giftCardRequestVo = giftCardService.createGiftCardRequest(orderHistoryDto, goodsDto, userDto, tradeStandByDtoList);

        // 2.0 상품권 발행 요청
        PongiftApiResponse pongiftApiResponse = giftCardService.giftCardCreateRequest(giftCardRequestVo, orderHistoryDto.getOrderBuyType());
        if (!pongiftApiResponse.getResponse().getCode().equals(PongiftApiResponse.ResponseCode.OK.getCode())) {
            throw new RuntimeException();
        }

        return pongiftApiResponse;
    }
}
