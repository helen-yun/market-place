package com.pongift20.marketplace.backend.goods.controller;

import com.pongift20.marketplace.backend.goods.model.vo.GoodsVo;
import com.pongift20.marketplace.backend.goods.model.vo.NextToken;
import com.pongift20.marketplace.backend.goods.model.dto.GoodsDto;
import com.pongift20.marketplace.backend.goods.service.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/goods")
public class GoodsController {
    private final GoodsService goodsService;

    /**
     * 상품 목록 조회
     *
     * @param nextTokenString 페이징 NextToken
     * @param keyword         검색어
     * @param category        카테고리 (categoryId, =BAH)
     * @param addr1           1차주소 (=서울)
     * @param addr2           2차주소 (=종로구)
     * @param addr3           3차주소 (=청운동)
     * @param sortBy          정렬 필드
     * @param sortOrder       정렬 순서 (desc, asc)
     */
    // TODO: 가격 정수형 반환?
    @GetMapping("")
    public ResponseEntity<GoodsVo> goodsList(@RequestParam(required = false, value = "nextToken") String nextTokenString,
                                             @RequestParam(required = false, value = "keyword") String keyword,
                                             @RequestParam(required = false, value = "category") String category,
                                             @RequestParam(required = false, value = "addr1") String addr1,
                                             @RequestParam(required = false, value = "addr2") String addr2,
                                             @RequestParam(required = false, value = "addr3") String addr3,
                                             @RequestParam(defaultValue = "goodsSeq", value = "sortBy") String sortBy,
                                             @RequestParam(defaultValue = "desc", value = "sortOrder") String sortOrder) {
        if (keyword != null && keyword.trim().length() < 2) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        List<GoodsDto> goodsDtoList;
        if (nextTokenString != null) { // nextToken이 있는 경우
            NextToken nextToken = goodsService.decodeNextToken(nextTokenString);
            if (nextToken != null) {
                goodsDtoList = goodsService.selectGoodsListByNextToken(nextToken);
                sortBy = nextToken.getSortBy();
                sortOrder = nextToken.getSortOrder();
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else { // 없는 경우 초기값 세팅
            goodsDtoList = goodsService.selectGoodsList(sortBy, sortOrder, keyword, category, addr1, addr2, addr3);
        }

        // 내려줄 nextToken 세팅
        NextToken returnNextToken = goodsService.createNextToken(goodsDtoList, keyword, category, addr1, addr2, addr3, sortBy, sortOrder);
        String encodeNextToken = goodsService.encodeNextToken(returnNextToken);

        GoodsVo.Meta meta = GoodsVo.Meta.builder()
                .totalCount(goodsService.countGoodsList(keyword, category, addr1, addr2, addr3))
                .nextToken(encodeNextToken)
                .build();

        if (goodsDtoList.size() == 31) goodsDtoList.remove(goodsDtoList.size() - 1);
        GoodsVo goodsVo = GoodsVo.builder()
                .list(goodsDtoList)
                .meta(meta)
                .build();
        return new ResponseEntity<>(goodsVo, HttpStatus.OK);
    }

    /**
     * 상품 상세 조회
     *
     * @param goodsSeq 상품 pk
     */
    @GetMapping("/{goodsSeq}")
    public ResponseEntity<GoodsVo> goodsList(@PathVariable(value = "goodsSeq") long goodsSeq) {
        GoodsDto goodsDto = goodsService.selectGoodsDetail(goodsSeq);
        if (goodsDto == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        GoodsVo goodsVo = GoodsVo.builder()
                .detail(goodsDto)
                .build();
        return new ResponseEntity<>(goodsVo, HttpStatus.OK);
    }
}
