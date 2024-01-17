package com.pongift20.marketplace.backend.order.service;

import com.pongift20.marketplace.backend.common.exception.ValidationException;
import com.pongift20.marketplace.backend.common.response.KcpApiResponse;
import com.pongift20.marketplace.backend.internal.api.KcpApiClient;
import com.pongift20.marketplace.backend.order.mapper.OrderMapper;
import com.pongift20.marketplace.backend.order.model.dto.*;
import com.pongift20.marketplace.backend.utils.AES256Util;
import com.pongift20.marketplace.backend.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderMapper orderMapper;
    private final KcpApiClient kcpApiClient;

    /**
     * 주문 데이터 생성 (주문대기)
     *
     * @param orderStandByDto 주문 데이터
     * @throws ValidationException   유효성 검사 실패 exception
     * @throws RuntimeException      기타 insert 관련 에러
     * @throws DuplicateKeyException orderNo가 중복된 경우
     */
    @Transactional
    public void insertStandByOrder(OrderStandByDto orderStandByDto) throws ValidationException, RuntimeException, DuplicateKeyException {
        if (!validateStandByOrder(orderStandByDto)) throw new ValidationException();
        long result = orderMapper.insertStandByOrder(orderStandByDto);
        if (result <= 0) throw new RuntimeException();
    }

    /**
     * 주문 완료 이력 생성
     *
     * @param orderHistoryDto 주문 완료 데이터
     */
    @Transactional
    public void insertOrderHistory(OrderHistoryDto orderHistoryDto) throws ValidationException, RuntimeException {
        if (orderHistoryDto.getOrderTransactNo() == null) throw new ValidationException();
        long result = orderMapper.insertOrderHistory(orderHistoryDto);
        if (result <= 0) throw new RuntimeException();
    }

    /**
     * 주문 완료 건 수정
     *
     * @param orderHistoryDto 주문 수정 데이터
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void modifyOrder(OrderHistoryDto orderHistoryDto) {
        if (orderHistoryDto.getOrderNo() == null) throw new ValidationException();
        long historyResult = orderMapper.updateOrderHistory(orderHistoryDto);
        long walletResult = orderMapper.updateGiftCardWallet(orderHistoryDto);

        if(historyResult <= 0 || walletResult <= 0){
            log.error("주문 건 업데이트 에러");
            throw new RuntimeException();
        }
    }

    /**
     * 거래 이력 삽입
     *
     * @param tradeStandByDtoList 거래 이력
     * @throws RuntimeException
     */
    @Transactional
    public void insertStandByTradeList(List<TradeStandByDto> tradeStandByDtoList) throws RuntimeException {
        long result;
        try {
            encodePhoneNumberByTradeList(tradeStandByDtoList);
            result = orderMapper.insertStandByTradeList(tradeStandByDtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
        if (result <= 0) throw new RuntimeException();
    }

    /**
     * 거래 완료 이력 삽입
     *
     * @param tradeHistoryDtoList 거래 완료 이력
     * @throws RuntimeException
     */
    @Transactional
    public void insertTradeHistoryList(List<TradeHistoryDto> tradeHistoryDtoList) throws RuntimeException {
        long result;
        try {
            result = orderMapper.insertTradeHistoryList(tradeHistoryDtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
        if (result <= 0) throw new RuntimeException();
    }

    /**
     * 거래 목록(대기상태) 주문 PK 기준 조회
     *
     * @param orderStandBySeq 주문 PK (대기상태)
     * @return List<TradeStandByVo>
     */
    public List<TradeStandByDto> selectTradeStandByListByOrderStandBySeq(long orderStandBySeq) {
        return orderMapper.selectTradeStandByListByOrderStandBySeq(orderStandBySeq);
    }

    /**
     * 거래 목록 수신자 휴대폰번호 암호화
     *
     * @param tradeStandByDtoList 수신자 목록
     * @throws Exception
     */
    public void encodePhoneNumberByTradeList(List<TradeStandByDto> tradeStandByDtoList) throws Exception {
        AES256Util aes256Util = new AES256Util("UTF-8");
        for (TradeStandByDto tradeStandByDto : tradeStandByDtoList) {
            tradeStandByDto.setTradeReceiverPhoneNumber(aes256Util.aesEncode(tradeStandByDto.getTradeReceiverPhoneNumber()));
        }
    }

    /**
     * 주문번호 기준 주문 조회 (대기상태)
     *
     * @param orderNo 주문번호
     * @return OrderVo
     */
    public OrderStandByDto selectStandByOrderByOrderNo(String orderNo) {
        return orderMapper.selectStandByOrderByOrderNo(orderNo);
    }

    /**
     * 주문번호 기준 주문 조회 (결제 완료)
     *
     * @param orderNo 주문번호
     * @return OrderVo
     */
    public OrderHistoryDto selectOrderHistoryByOrderNo(String orderNo, String userUid) {
        return orderMapper.selectOrderHistoryByOrderNo(orderNo, userUid);
    }

    /**
     * 주문번호 생성 프로시저 호출
     *
     * @return orderNo 주문번호
     */
    public String createOrderNo() {
        String randomNo = String.valueOf(RandomUtil.getRandomIntWithSize(6));
        OrderNoDto orderNoDto = new OrderNoDto();
        orderNoDto.setRandomNo(randomNo);

        orderMapper.createOrderNo(orderNoDto);
        if (!orderNoDto.getResultCode().equals("0000")) {
            throw new RuntimeException();
        }
        return orderNoDto.getOrderNo();
    }

    /**
     * 주문 대기 order 객체 유효성 검사
     *
     * @param orderStandByDto 주문 대기 객체
     * @return boolean 이상 없는 경우 true
     */
    private boolean validateStandByOrder(OrderStandByDto orderStandByDto) {
        if (orderStandByDto.getOrderNo() == null) {
            log.error("### 주문번호 미존재");
            return false;
        }
        if (orderStandByDto.getGoodsSeq() <= 0) {
            log.error("### 상품 PK 미존재");
            return false;
        }
        if (orderStandByDto.getOrderCount() <= 0) {
            log.error("주문 수량 0 건 이하 오류");
            return false;
        }

        return true;
    }

    public KcpApiResponse requestKcpCancelOrder(String tno) throws RuntimeException, NotFoundException {
        return kcpApiClient.requestCancelOrder(tno);
    }
}
