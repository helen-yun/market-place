package com.pongift20.marketplace.backend.order.service;

import com.pongift20.marketplace.backend.order.model.dto.OrderHistoryDto;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("dev")
@Slf4j
class MyOrderServiceTest {
    @Autowired
    private MyOrderService myOrderService;
    private final int PAGE_SIZE = 31;

    @Test
    void getLastOrderSeq() {
        // GIVEN
        List<OrderHistoryDto> orderHistoryDtoList = new ArrayList<>();
        for (int i = 0; i < PAGE_SIZE - 1; i++) {
            orderHistoryDtoList.add(OrderHistoryDto.builder().orderHistorySeq(i).build());
        }

        // WHEN
        long smallerThanPageSize = myOrderService.getLastOrderSeq(orderHistoryDtoList); // 페이징 사이즈보다 작은 경우

        orderHistoryDtoList.add(OrderHistoryDto.builder().orderHistorySeq(30).build());
        long samePageSize= myOrderService.getLastOrderSeq(orderHistoryDtoList); // 페이징 사이즈와 같은 경우

        orderHistoryDtoList.add(OrderHistoryDto.builder().orderHistorySeq(31).build());
        long largerThanPageSize= myOrderService.getLastOrderSeq(orderHistoryDtoList); // 페이징 사이즈보다 큰 경우

        // THEN
        Assertions.assertThat(smallerThanPageSize).isEqualTo(0);
        Assertions.assertThat(samePageSize).isEqualTo(PAGE_SIZE - 2);
        Assertions.assertThat(largerThanPageSize).isEqualTo(PAGE_SIZE);
    }
}