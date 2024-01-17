package com.pongift20.marketplace.backend.goods.service;

import com.pongift20.marketplace.backend.goods.model.vo.NextToken;
import com.pongift20.marketplace.backend.goods.model.dto.GoodsDto;
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
class GoodsServiceTest {
    @Autowired
    private GoodsService goodsService;

    String keyword = "키워드";
    String category = "BAH";
    String addr1 = "서울";
    String addr2 = "종로구";
    String addr3 = "청운동";
    String sortBy = "goodsSeq";
    String sortOrder = "desc";

    @Test
    void encodeNextToken() {
        // GIVEN
        List<GoodsDto> goodsDtoList = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            goodsDtoList.add(new GoodsDto());
        }
        NextToken nextToken = goodsService.createNextToken(goodsDtoList, keyword, category, addr1, addr2, addr3, sortBy, sortOrder);
        String encode = goodsService.encodeNextToken(nextToken);

        // WHEN
        NextToken decodeNextToken = goodsService.decodeNextToken(encode);

        // THEN
        Assertions.assertThat(nextToken).isEqualTo(decodeNextToken);
    }
}