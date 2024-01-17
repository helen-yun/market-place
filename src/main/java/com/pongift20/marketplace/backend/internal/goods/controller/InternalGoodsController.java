package com.pongift20.marketplace.backend.internal.goods.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pongift20.marketplace.backend.internal.goods.dto.NewGoodsDto;
import com.pongift20.marketplace.backend.goods.service.GoodsService;
import com.pongift20.marketplace.backend.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/goods")
public class InternalGoodsController {
    private final GoodsService goodsService;

    /**
     * 상품 상세 조회
     * @param goodsId 상품 ID
     * @return
     */
    @GetMapping("")
    public ResponseEntity<Object> selectGoods(@RequestParam(value = "goodsId") String goodsId) throws JsonProcessingException {
        log.info("==================================================");
        log.info("제휴몰 상품 조회 : {}", goodsId);
        log.info("//================================================");
        NewGoodsDto result = goodsService.selectGoods(goodsId);
        if(result == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        JsonUtils.toJson(result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 상품 추가
     * @param goodsReq
     * @return
     */
    @PostMapping("")
    public ResponseEntity<Object> createGoods(@RequestBody NewGoodsDto goodsReq){
        log.info("==================================================");
        log.info("제휴몰 상품 등록 : {}", goodsReq);
        log.info("//================================================");
        goodsService.createGoods(goodsReq);
        return new ResponseEntity<>(goodsReq.getGoodsSeq(), HttpStatus.OK);
    }

    /**
     * 상품 수정
     * @param goodsReq
     * @return
     */
    @PutMapping("")
    public ResponseEntity<Object> updateGoods(@RequestBody NewGoodsDto goodsReq){
        log.info("==================================================");
        log.info("제휴몰 상품 수정 : {}", goodsReq);
        log.info("//================================================");
        long result = goodsService.updateGoods(goodsReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * 상품 전시상태 수정
     * @return
     */
    @PutMapping("/exhibits")
    public ResponseEntity<Object> updateGoodsExhibits(@RequestParam(value = "goodsId") String goodsId){
        log.info("==================================================");
        log.info("제휴몰 상품 전시 상태 수정 : {}", goodsId);
        log.info("//================================================");
        long result = goodsService.updateGoodsExhibits(goodsId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
