package com.pongift20.marketplace.backend.giftcard.controller;

import com.pongift20.marketplace.backend.giftcard.model.vo.GiftWalletVo;
import com.pongift20.marketplace.backend.giftcard.model.dto.GiftCardDto;
import com.pongift20.marketplace.backend.giftcard.service.GiftCardService;
import com.pongift20.marketplace.backend.user.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/giftcard")
public class GiftCardController {
    private final GiftCardService giftCardService;

    /**
     * 내 상품권 조회
     *
     * @return
     */
    @GetMapping("")
    public ResponseEntity<GiftWalletVo> getGiftCardList(@RequestParam(defaultValue = "0", value = "nextToken") long lastSeq,
                                                        @RequestParam(value = "isUsed") boolean giftNoUsed,
                                                        Authentication authentication) {
        // 사용자 세션 조회
        if (authentication == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        UserDto userDto = (UserDto) authentication.getDetails();

        List<GiftCardDto> giftCardDtoList = giftCardService.selectGiftCardList(userDto.getUserUid(), giftNoUsed, lastSeq);
        long giftWalletCount = giftCardService.countGiftWallet(userDto.getUserUid(), giftNoUsed);

        GiftWalletVo.Meta meta = GiftWalletVo.Meta.builder()
                .totalCount(giftWalletCount)
                .nextToken(giftCardService.getLastOrderSeq(giftCardDtoList))
                .build();

        if (giftCardDtoList.size() == 31) giftCardDtoList.remove(giftCardDtoList.size() - 1);
        GiftWalletVo giftWalletVo = GiftWalletVo.builder()
                .list(giftCardDtoList)
                .meta(meta)
                .build();
        return new ResponseEntity<>(giftWalletVo, HttpStatus.OK);
    }
    /**
     * 상품권 상세 조회
     * @author sjeg
     * @param {number} giftWalletSeq 상품 pk
     */
    @GetMapping("/{giftCardSeq}")
    public ResponseEntity<GiftWalletVo> getGiftCard(@PathVariable(value = "giftCardSeq") long giftWalletSeq,
                                             Authentication authentication) {
        // 사용자 세션 조회
        if (authentication == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        UserDto userDto = (UserDto) authentication.getDetails();

        GiftCardDto giftCardDto = giftCardService.selectGiftCardDetail(userDto.getUserUid(), giftWalletSeq);
        if (giftCardDto == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        GiftWalletVo giftWalletVo = GiftWalletVo.builder()
                .detail(giftCardDto)
                .build();
        return new ResponseEntity<>(giftWalletVo, HttpStatus.OK);
    }

}
