package com.pongift20.marketplace.backend.internal.giftcard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pongift20.marketplace.backend.giftcard.service.GiftCardService;
import com.pongift20.marketplace.backend.internal.giftcard.controller.dto.GiftCardDto;
import com.pongift20.marketplace.backend.internal.giftcard.controller.dto.GiftCardExtensionDto;
import com.pongift20.marketplace.backend.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/giftcard")
public class InternalGiftCardController {
    private final GiftCardService giftCardService;
    // TODO: 상품권 외부 연동

    /**
     * 상품권 유효기간 연장
     */
    @PostMapping("/extension")
    public ResponseEntity<Object> updateExpiryDate(@RequestBody GiftCardExtensionDto giftCardExtensionDto) throws JsonProcessingException {
        log.info("==================================================");
        log.info("상품권 유효기간 연장 : {}", JsonUtils.toJson(giftCardExtensionDto));
        log.info("//================================================");
        giftCardService.extendGiftCardExpirationDate(giftCardExtensionDto.getGiftNo(), giftCardExtensionDto.getExpiryDate());
        return new ResponseEntity<>(giftCardExtensionDto, HttpStatus.OK);
    }

    /**
     * 상품권 사용 처리
     *
     * @param giftCardDto 2.0 상품권 사용 처리 객체
     */
    @PostMapping("/use")
    public ResponseEntity<GiftCardDto> useGiftCard(@RequestBody GiftCardDto giftCardDto) throws JsonProcessingException {
        log.info("==================================================");
        log.info("상품권 사용 처리: {}", JsonUtils.toJson(giftCardDto));
        log.info("//================================================");
        giftCardService.useGiftCard(giftCardDto.getGiftNo());
        return new ResponseEntity<>(giftCardDto, HttpStatus.OK);
    }

    /**
     * 상품권 사용 취소 처리
     *
     * @param giftCardDto 2.0 상품권 사용 처리 객체
     */
    @PostMapping("/cancel")
    public ResponseEntity<GiftCardDto> cancelGiftCard(@RequestBody GiftCardDto giftCardDto) throws JsonProcessingException {
        log.info("==================================================");
        log.info("상품권 사용 취소 처리: {}", JsonUtils.toJson(giftCardDto));
        log.info("//================================================");
        giftCardService.cancelGiftCard(giftCardDto.getGiftNo());
        return new ResponseEntity<>(giftCardDto, HttpStatus.OK);
    }
}
