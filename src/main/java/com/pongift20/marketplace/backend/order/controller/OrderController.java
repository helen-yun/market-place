package com.pongift20.marketplace.backend.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pongift20.marketplace.backend.common.exception.ValidationException;
import com.pongift20.marketplace.backend.common.response.PongiftApiResponse;
import com.pongift20.marketplace.backend.giftcard.model.vo.PongiftSendGiftCardResponseVo;
import com.pongift20.marketplace.backend.giftcard.model.dto.GiftWalletDto;
import com.pongift20.marketplace.backend.giftcard.service.GiftCardService;
import com.pongift20.marketplace.backend.goods.model.dto.GoodsDto;
import com.pongift20.marketplace.backend.goods.service.GoodsService;
import com.pongift20.marketplace.backend.order.code.OrderCode;
import com.pongift20.marketplace.backend.order.model.dto.OrderHistoryDto;
import com.pongift20.marketplace.backend.order.model.dto.OrderStandByDto;
import com.pongift20.marketplace.backend.order.model.vo.*;
import com.pongift20.marketplace.backend.order.service.MainOrderService;
import com.pongift20.marketplace.backend.order.service.MyOrderService;
import com.pongift20.marketplace.backend.order.service.OrderService;
import com.pongift20.marketplace.backend.user.model.dto.UserDto;
import com.pongift20.marketplace.backend.user.service.UserService;
import com.pongift20.marketplace.backend.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final MainOrderService mainOrderService;
    private final MyOrderService myOrderService;
    private final GoodsService goodsService;
    private final UserService userService;
    private final GiftCardService giftCardService;

    /**
     * 내 구매내역 조회
     *
     * @param lastSeq        조회 마지막 PK (페이징)
     * @param authentication 현재 세션 데이터
     * @return MyOrderDto
     */
    @GetMapping("")
    public ResponseEntity<OrderVo> getMyOrderList(@RequestParam(defaultValue = "0", value = "nextToken") long lastSeq,
                                                  Authentication authentication) {
        // 사용자 세션 조회
        if (authentication == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        UserDto userDto = (UserDto) authentication.getDetails();

        // UID 기준 주문 내역 조회
        List<OrderHistoryDto> orderHistoryDtoList = myOrderService.selectOrderByUserUid(userDto.getUserUid(), lastSeq);

        OrderVo.Meta meta = OrderVo.Meta.builder()
                .nextToken(myOrderService.getLastOrderSeq(orderHistoryDtoList))
                .build();

        if (orderHistoryDtoList.size() == 31) orderHistoryDtoList.remove(orderHistoryDtoList.size() - 1);
        OrderVo orderVo = OrderVo.builder()
                .meta(meta)
                .list(orderHistoryDtoList)
                .build();
        return new ResponseEntity<>(orderVo, HttpStatus.OK);
    }

    /**
     * 주문 생성 (주문대기)
     *
     * @param standByOrderRequestVo 주문 생성 요청 dto
     */
    @PostMapping("/standby")
    public ResponseEntity<StandByOrderResponseVo> standBy(@Valid @RequestBody StandByOrderRequestVo standByOrderRequestVo, Authentication authentication) {
        // 사용자 세션 조회
        if (authentication == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        UserDto userDto = (UserDto) authentication.getDetails();

        // 상품 정보 조회
        long goodsSeq = standByOrderRequestVo.getGoodsSeq();
        GoodsDto goodsDto = goodsService.selectGoodsDetail(goodsSeq);
        if (goodsDto == null) return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);

        // 주문번호 생성
        String orderNo = orderService.createOrderNo();

        // 주문 정보 (주문대기)
        OrderStandByDto orderStandByDto = OrderStandByDto.builder()
                .userUid(userDto.getUserUid())
                .goodsSeq(goodsDto.getGoodsSeq())
                .storeSeq(goodsDto.getStoreSeq())
                .orderNo(orderNo)
                .orderBuyType(standByOrderRequestVo.getBuyType().getCode())
                .orderPaymentAmount(standByOrderRequestVo.getPaymentAmount(goodsDto.getGoodsSalePrice(), standByOrderRequestVo.getOrderCount()))
                .orderCount(standByOrderRequestVo.getOrderCount())
                .storeName(goodsDto.getStoreName())
                .goodsName(goodsDto.getGoodsName())
                .goodsSalePrice(goodsDto.getGoodsSalePrice())
                .goodsRetailPrice(goodsDto.getGoodsRetailPrice())
                .goodsModifiedAt(goodsDto.getModifiedAt() == null ? goodsDto.getCreateAt() : goodsDto.getModifiedAt())
                .createdAt(new Date())
                .build();

        try {
            // 주문 정보 생성 (주문대기)
            if (OrderCode.BuyType.BUY.equals(standByOrderRequestVo.getBuyType())) {
                mainOrderService.createStandByOrder(orderStandByDto, userDto);
            } else {
                mainOrderService.createGiftStandByOrder(orderStandByDto, standByOrderRequestVo.getReceiverList());
            }
        } catch (ValidationException e) {
            log.error("### 유효성 검사 통과 실패");
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (DuplicateKeyException e) {
            log.error("### 주문 번호 중복");
            return new ResponseEntity<>(null, HttpStatus.TEMPORARY_REDIRECT);
        } catch (Exception e) {
            log.error(e.getClass().getName());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 주문 생성 내용 리턴값 생성
        StandByOrderResponseVo.Order order = StandByOrderResponseVo.Order.builder()
                .orderCount(orderStandByDto.getOrderCount())
                .orderNo(orderStandByDto.getOrderNo())
                .orderPaymentAmount(orderStandByDto.getOrderPaymentAmount())
                .createdAt(orderStandByDto.getCreatedAt())
                .build();

        StandByOrderResponseVo.Goods goods = StandByOrderResponseVo.Goods.builder()
                .goodsName(goodsDto.getGoodsName())
                .storeName(goodsDto.getStoreName())
                .goodsSalePrice(goodsDto.getGoodsSalePrice())
                .goodsRetailPrice(goodsDto.getGoodsRetailPrice())
                .build();

        StandByOrderResponseVo standByOrderResponseVo = StandByOrderResponseVo.builder()
                .order(order)
                .goods(goods)
                .build();

        return new ResponseEntity<>(standByOrderResponseVo, HttpStatus.OK);
    }

    /**
     * 결제 완료 처리
     *
     * @param purchaseOrderRequestVo 결제 완료 데이터 (KCP tno)
     */
    @PostMapping(value = "/purchase", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> purchase(@Valid PurchaseOrderRequestVo purchaseOrderRequestVo) {
        // TODO: 구매취소 가능 기간?

        // 주문번호(orderNo) 기준 주문대기 건 조회
        OrderStandByDto orderStandByDto = orderService.selectStandByOrderByOrderNo(purchaseOrderRequestVo.getPurchaseCode());

        // 주문 대기 상태 금액과 실 결제 금액이 다른 경우 400
        if (orderStandByDto.getOrderPaymentAmount().compareTo(purchaseOrderRequestVo.getPrice()) != 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        // 결제 완료 주문 생성
        Date nowDate = new Date();
        OrderHistoryDto orderHistoryDto = OrderHistoryDto.builder()
                .userUid(orderStandByDto.getUserUid())
                .orderNo(orderStandByDto.getOrderNo())
                .goodsSeq(orderStandByDto.getGoodsSeq())
                .storeSeq(orderStandByDto.getStoreSeq())
                .storeName(orderStandByDto.getStoreName())
                .goodsName(orderStandByDto.getGoodsName())
                .goodsSalePrice(orderStandByDto.getGoodsSalePrice())
                .goodsRetailPrice(orderStandByDto.getGoodsRetailPrice())
                .goodsModifiedAt(orderStandByDto.getGoodsModifiedAt())
                .createdAt(nowDate)
                .modifiedAt(nowDate)
                .orderBuyType(orderStandByDto.getOrderBuyType())
                .orderPaymentAmount(orderStandByDto.getOrderPaymentAmount())
                .orderState(OrderCode.State.PURCHASE.getCode())
                .orderTransactNo(purchaseOrderRequestVo.getTno())
                .orderApproveNo(purchaseOrderRequestVo.getPurchaseAuthorizationCode())
                .orderCount(orderStandByDto.getOrderCount())
                .build();
        GoodsDto goodsDto = goodsService.selectGoodsDetail(orderHistoryDto.getGoodsSeq());
        UserDto userDto = userService.selectUserByUserUid(orderHistoryDto.getUserUid());
        PongiftApiResponse pongiftApiResponse;
        try {
            // 결제 완료 및 발송 처리
            pongiftApiResponse = mainOrderService.insertPurchaseAndRequestGiftCard(orderHistoryDto, orderStandByDto.getOrderStandBySeq(), goodsDto, userDto);
        } catch (ValidationException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 나에게 구매하기인 경우 내상품권에 저장
        // 20231027 보낸 선물 추가로 내역 저장
        try {
            // 2.0 상품권 저장
            PongiftSendGiftCardResponseVo[] pongiftSendGiftCardResponseVos = JsonUtils.fromJson(pongiftApiResponse.getData().toString(), PongiftSendGiftCardResponseVo[].class);
            List<GiftWalletDto> giftWalletDtoList = Arrays.stream(pongiftSendGiftCardResponseVos)
                    .map(pongiftSendGiftCardResponseVo -> GiftWalletDto.builder()
                            .giftNo(pongiftSendGiftCardResponseVo.getGiftNo())
                            .userUid(userDto.getUserUid())
                            .goodsSeq(goodsDto.getGoodsSeq())
                            .goodsName(goodsDto.getGoodsName())
                            .giftUrl(giftCardService.getPongift20GiftCardUrl(pongiftSendGiftCardResponseVo.getPublicCode()))
                            .giftExpiryDate(pongiftSendGiftCardResponseVo.getExpiryDate())
                            .giftType(OrderCode.BuyType.BUY.getCode().equals(orderHistoryDto.getOrderBuyType()) ? OrderCode.BuyType.BUY.getCode(): OrderCode.BuyType.GIFT.getCode())
                            .orderNo(orderHistoryDto.getOrderNo())
                            .build())
                    .collect(Collectors.toList());
            giftCardService.insertGiftWalletList(giftWalletDtoList);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(pongiftApiResponse, HttpStatus.OK);
    }

    /**
     * 주문 완료 건 데이터 조회
     *
     * @param orderNo        주문 번호
     * @param authentication 현재 세션 데이터
     * @return PurchaseResultDto
     */
    @GetMapping("/{orderNo}")
    public ResponseEntity<OrderVo> getPurchaseOrder(@PathVariable(value = "orderNo") String orderNo, Authentication authentication) {
        // 사용자 세션 조회
        if (authentication == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        UserDto userDto = (UserDto) authentication.getDetails();

        // 주문번호, UID 기준 결제 완료 건 조회
        OrderHistoryDto orderHistoryDto = orderService.selectOrderHistoryByOrderNo(orderNo, userDto.getUserUid());
        OrderVo.Meta meta = OrderVo.Meta.builder()
                .buyerName(userDto.getUserName())
                .build();
        OrderVo orderVo = OrderVo.builder()
                .meta(meta)
                .order(orderHistoryDto)
                .build();
        return new ResponseEntity<>(orderVo, HttpStatus.OK);
    }
}
