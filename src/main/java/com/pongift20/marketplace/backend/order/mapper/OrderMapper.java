package com.pongift20.marketplace.backend.order.mapper;

import com.pongift20.marketplace.backend.order.model.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     * 사용자 UID 기준 주문 내역 조회
     *
     * @param userUid 사용자 UID
     * @return List<OrderVo>
     */
    List<OrderHistoryDto> selectOrderByUserUid(String userUid, long lastSeq, int pageSize);

    /**
     * 미발송 주문 건 조회
     *
     * @return List<OrderHistoryVo>
     */
    List<OrderHistoryDto> selectNotSendOrderHistoryList();

    /**
     * 주문 데이터 생성 (주문대기)
     *
     * @param orderStandByDto 주문 데이터
     * @return long 결과
     */
    long insertStandByOrder(OrderStandByDto orderStandByDto);

    /**
     * 주문 완료
     *
     * @param orderHistoryDto 주문 완료 데이터
     * @return long 결과
     */
    long insertOrderHistory(OrderHistoryDto orderHistoryDto);

    /**
     * 주문 완료 수정
     *
     * @param orderHistoryDto 주문 수정 데이터
     * @return long 결과
     */
    long updateOrderHistory(OrderHistoryDto orderHistoryDto);
    /**
     * 취소 완료 업데이트
     */
    long updateGiftCardWallet(OrderHistoryDto orderHistoryDto);

    /**
     * 주문번호 생성 (create_order_no 프로시저 호출)
     *
     * @param orderNoDto 생성한 orderNo 받을 객체
     */
    void createOrderNo(OrderNoDto orderNoDto);

    /**
     * 주문번호 기준 주문 조회 (대기상태)
     *
     * @param orderNo 주문번호
     * @return OrderStandByVo 주문객체
     */
    OrderStandByDto selectStandByOrderByOrderNo(String orderNo);
    
    /**
     * 결제 완료 주문 조회
     *
     * @param orderNo 주문번호
     * @return OrderHistoryVo 주문객체
     */
    OrderHistoryDto selectOrderHistoryByOrderNo(String orderNo, String userUid);

    /**
     * 거래 이력 생성
     *
     * @param tradeStandByDtoList 거래이력 리스트
     * @return long 결과
     */
    long insertStandByTradeList(List<TradeStandByDto> tradeStandByDtoList);

    /**
     * 거래 완료 이력 생성
     *
     * @param tradeHistoryDtoList 거래이력 리스트
     * @return long 결과
     */
    long insertTradeHistoryList(List<TradeHistoryDto> tradeHistoryDtoList);

    /**
     * 거래 목록 조회 (주문 대기)
     *
     * @param orderStandBySeq 주문 대기 이력 PK
     * @return List<TradeStandByVo> 거래 대기 중 이력
     */
    List<TradeStandByDto> selectTradeStandByListByOrderStandBySeq(long orderStandBySeq);

    /**
     * 거래 목록 조회 (결제 완료)
     *
     * @param orderHistorySeq 주문 완료 이력 PK
     * @return List<TradeStandByVo> 거래 목록
     */
    List<TradeHistoryDto> selectTradeHistoryListByOrderHistorySeq(long orderHistorySeq);

}
