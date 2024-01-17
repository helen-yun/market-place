package com.pongift20.marketplace.backend.order.service;

import com.pongift20.marketplace.backend.order.mapper.OrderMapper;
import com.pongift20.marketplace.backend.order.model.dto.OrderHistoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class MyOrderService {
    private final int PAGE_SIZE = 31;
    private final OrderMapper orderMapper;

    public List<OrderHistoryDto> selectOrderByUserUid(String userUid, long lastSeq) {
        return orderMapper.selectOrderByUserUid(userUid, lastSeq, PAGE_SIZE);
    }

    public long getLastOrderSeq(List<OrderHistoryDto> orderHistoryDtoList) {
        if (orderHistoryDtoList.size() <= PAGE_SIZE - 1) {
            return 0;
        }
        OrderHistoryDto lastOrderHistoryDto;
        if (orderHistoryDtoList.size() == PAGE_SIZE) {
            lastOrderHistoryDto = orderHistoryDtoList.get(PAGE_SIZE - 2);
        } else {
            lastOrderHistoryDto = orderHistoryDtoList.get(orderHistoryDtoList.size() - 1);
        }
        return lastOrderHistoryDto.getOrderHistorySeq();
    }
}
