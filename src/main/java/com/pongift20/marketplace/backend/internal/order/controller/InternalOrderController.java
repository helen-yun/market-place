package com.pongift20.marketplace.backend.internal.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pongift20.marketplace.backend.common.response.KcpApiResponse;
import com.pongift20.marketplace.backend.common.response.PongiftApiResponse;
import com.pongift20.marketplace.backend.order.code.OrderCode;
import com.pongift20.marketplace.backend.order.model.dto.OrderHistoryDto;
import com.pongift20.marketplace.backend.order.service.OrderService;
import com.pongift20.marketplace.backend.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/order")
public class InternalOrderController {
    private final OrderService orderService;

    /**
     * 주문 취소
     *
     * @param orderNo 제휴몰 주문번호 (order_no)
     */
    @PostMapping("/cancel/{orderNo}")
    public ResponseEntity<PongiftApiResponse> cancelOrder(@PathVariable(value = "orderNo") String orderNo) throws JsonProcessingException {
        log.info("==================================================");
        log.info("제휴몰 주문 취소 orderNo : {}", orderNo);
        log.info("//================================================");
        PongiftApiResponse pongiftApiResponse = new PongiftApiResponse();
        KcpApiResponse kcpApiResponse;
        OrderHistoryDto orderHistoryDto = orderService.selectOrderHistoryByOrderNo(orderNo, null);
        try {
            String tno = orderHistoryDto.getOrderTransactNo();
            kcpApiResponse = orderService.requestKcpCancelOrder(tno);
        } catch (RuntimeException | NotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (kcpApiResponse.getResCd().equals("0000")) {
            orderHistoryDto.setOrderState(OrderCode.State.RETURN.getCode());
            orderService.modifyOrder(orderHistoryDto);
        }
        pongiftApiResponse.setData(kcpApiResponse);

        log.info("주문 취소 response : {} ", JsonUtils.toJson(kcpApiResponse));
        return new ResponseEntity<>(pongiftApiResponse, HttpStatus.OK);
    }
}
