package com.pongift20.marketplace.backend.internal.store.controller;

import com.pongift20.marketplace.backend.common.response.PongiftApiResponse;
import com.pongift20.marketplace.backend.store.model.vo.StoreVo;
import com.pongift20.marketplace.backend.store.model.dto.StoreDto;
import com.pongift20.marketplace.backend.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/store")
public class InternalStoreController {
    private final StoreService storeService;

    /**
     * 매장 조회
     * @param storeSeq 어드민 매장 pk
     * @return
     */
    @GetMapping("")
    public ResponseEntity<Object> selectStore(@RequestParam(value = "storeSeq") long storeSeq){
        log.debug("==================================================");
        log.debug("제휴몰 매장 조회 : {}", storeSeq);
        log.debug("//================================================");
        StoreDto info = storeService.selectStore(storeSeq);
        if(info == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        PongiftApiResponse result = new PongiftApiResponse();
        result.setData(info);
        log.debug("response : {}", result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 매장 신규 등록
     * @param param 매장 정보
     * @return
     */
    @PostMapping("")
    public ResponseEntity<Object> createStore(@RequestBody StoreVo param){
        log.debug("==================================================");
        log.debug("제휴몰 매장 등록 : {}", param);
        log.debug("//================================================");
        if(param.getStoreSeq() < 0){
            log.debug("storeSeq is required");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }else if(StringUtils.isEmpty(param.getServiceName())){
            log.debug("serviceName is required");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        StoreDto info = storeService.insertStore(param);
        if(info == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        PongiftApiResponse result = new PongiftApiResponse();
        result.setData(info);
        log.debug("response : {}", result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 매장 수정
     * @param param 매장 정보
     * @return
     */
    @PutMapping("")
    public ResponseEntity<Object> updateStore(@RequestBody StoreVo param){
        log.debug("==================================================");
        log.debug("제휴몰 매장 수정 : {}", param);
        log.debug("//================================================");
        if(param.getStoreSeq() < 0){
            log.debug("storeSeq is required");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }else if(StringUtils.isEmpty(param.getServiceName())){
            log.debug("serviceName is required");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        StoreDto info = storeService.updateStore(param);
        if(info == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        PongiftApiResponse result = new PongiftApiResponse();
        result.setData(info);
        log.debug("response : {}", result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
