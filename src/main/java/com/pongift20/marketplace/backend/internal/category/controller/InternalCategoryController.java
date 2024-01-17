package com.pongift20.marketplace.backend.internal.category.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pongift20.marketplace.backend.category.model.vo.CategoryGoodsVo;
import com.pongift20.marketplace.backend.category.model.vo.CategoryJusoVo;
import com.pongift20.marketplace.backend.category.model.dto.CategoryGoodsDto;
import com.pongift20.marketplace.backend.category.model.dto.CategoryJusoDto;
import com.pongift20.marketplace.backend.category.service.CategoryService;
import com.pongift20.marketplace.backend.common.response.PongiftApiResponse;
import com.pongift20.marketplace.backend.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.util.StringUtils;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/category")
public class InternalCategoryController {
    private final CategoryService categoryService;

    /*------------------------------------------------------상품-------------------------------------------------------*/

    /**
     * 제휴 카테고리 조회(카테고리관리 2.0)
     * @return
     */
    @GetMapping("/goods")
    public ResponseEntity<Object> marketCateGoodsList() throws JsonProcessingException {
        log.info("==================================================");
        log.info("제휴몰 상품 카테고리 조회");
        log.info("//================================================");
        List<CategoryGoodsDto> list = categoryService.selectCategoryGoodsAll("internal");
        if(list.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        PongiftApiResponse result = new PongiftApiResponse();
        result.setData(list);
        log.info("categoryList : {}", JsonUtils.toJson(list.size()));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * 상품 카테고리 개별 조회
     *
     */
    @GetMapping("/goods-detail")
    public ResponseEntity<Object> categoryGoodsDtl(@Valid @RequestParam("categoryId") String categoryId) throws JsonProcessingException {
        log.info("==================================================");
        log.info("제휴몰 상품 카테고리 상세 조회 : {} ", categoryId);
        log.info("//================================================");
        CategoryGoodsDto dtl = categoryService.selectCategoryGoodsDtl(categoryId);
        if(dtl == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        PongiftApiResponse result = new PongiftApiResponse();
        result.setData(dtl);
        log.info("response : {}", JsonUtils.toJson(result));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 상품 카테고리 업데이트
     *
     */
    @PutMapping("/goods")
    public ResponseEntity<Object> updateGoodsCategory(@Valid @RequestBody CategoryGoodsVo param) throws JsonProcessingException {
        log.info("==================================================");
        log.info("제휴몰 상품 카테고리 업데이트 : {}", param );
        log.info("//================================================");
        CategoryGoodsDto dtl = new CategoryGoodsDto();
        if(!param.getCategoryId().isEmpty()){
            dtl = categoryService.updateGoodsCategory(param);
        }else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        PongiftApiResponse result = new PongiftApiResponse();
        result.setData(dtl);
        log.info("response : {}", JsonUtils.toJson(result));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 상품 카테고리 추가
     *
     */
    @PostMapping("/goods")
    public ResponseEntity<Object> insertGoodsCategory(@Valid @RequestBody CategoryGoodsVo param) throws JsonProcessingException {
        log.info("==================================================");
        log.info("제휴몰 상품 카테고리 등록 : {}", param );
        log.info("//================================================");
        CategoryGoodsDto dtl = new CategoryGoodsDto();
        if(!param.getCategoryId().isEmpty()){
            dtl = categoryService.insertGoodsCategory(param);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        if(dtl == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        PongiftApiResponse result = new PongiftApiResponse();
        result.setData(dtl);
        log.info("response : {}", JsonUtils.toJson(result));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 상품 카테고리 삭제
     *
     */
    @DeleteMapping("/goods/{categoryId}")
    public ResponseEntity<Object> deleteGoodsCategory(@PathVariable("categoryId") String categoryId) throws JsonProcessingException {
        log.info("==================================================");
        log.info("제휴몰 상품 카테고리 삭제 : {}", categoryId );
        log.info("//================================================");
        long data = 0;
        if(!StringUtils.isEmpty(categoryId)) {
            data = categoryService.deleteGoodsCategory(categoryId);
        }else{
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        PongiftApiResponse result = new PongiftApiResponse();
        result.setData(data);
        log.info("response : {}", JsonUtils.toJson(result));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /*-------------------------------------------------------------------------------------------------------------
    * -------------------------------------주소--------------------------------------------------------------------*/

    /**
     * 주소 카테고리 전체 조회
     * @return
     */
    @GetMapping("/areas")
    public ResponseEntity<Object> marketCateAreaList() throws JsonProcessingException {
        log.info("==================================================");
        log.info("제휴몰 주소 카테고리 조회");
        log.info("//================================================");
        List<CategoryJusoDto> list = categoryService.selectCategoryJusoList();
        if(list.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        PongiftApiResponse result = new PongiftApiResponse();
        result.setData(list);
        log.info("categoryList : {}", JsonUtils.toJson(list.size()));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 주소 카테고리 뎁스별 조회
     *
     * @param addr1 depth1 주소명(파라미터 없을 경우 depth별 전체값 조회)
     * @param addr2 depth3 조회 시 필수값
     *소
     * */
    @GetMapping("/area-depth")
    public ResponseEntity<Object> categoryJuso(@RequestParam(required = false, value="addr1") String addr1,
                                               @RequestParam(required = false, value="addr2") String addr2) throws JsonProcessingException {
        log.info("==================================================");
        log.info("제휴몰 주소 카테고리 뎁스별 조회: {}", addr1+"&"+addr2);
        log.info("//================================================");
        List<CategoryJusoDto> list = categoryService.selectCategoryJuso(addr1, addr2);
        if(list.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        PongiftApiResponse result = new PongiftApiResponse();
        result.setData(list);
        log.info("categoryList : {}", JsonUtils.toJson(list.size()));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 주소 카테고리 개별 조회
     *
     */
    @GetMapping("/area-detail")
    public ResponseEntity<Object> categoryAreasDtl(@Valid @RequestParam("jusoCode") String jusoCode) throws JsonProcessingException {
        log.info("==================================================");
        log.info("제휴몰 주소 카테고리 개별 조회 : {}", jusoCode);
        log.info("//================================================");
        CategoryJusoDto dtl = categoryService.selectCategoryAreasDtl(jusoCode);
        if(dtl == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        PongiftApiResponse result = new PongiftApiResponse();
        result.setData(dtl);
        log.info("response : {}", JsonUtils.toJson(result));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 주소 카테고리 매장 상세 리스트
     *
     */
    @GetMapping("/store-areas")
    public ResponseEntity<Object> categoryStoreAreasList(@Valid @RequestParam("jusoCode") String jusoCode) throws JsonProcessingException {
        log.info("==================================================");
        log.info("매장 상세 주소 카테고리 리스트 : {}", jusoCode);
        log.info("//================================================");
        List<CategoryJusoDto> list = categoryService.selectCategoryAreasInfoList(jusoCode);
        if(list == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        PongiftApiResponse result = new PongiftApiResponse();
        result.setData(list);
        log.info("categoryList : {}", JsonUtils.toJson(list.size()));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 주소 카테고리 추가
     *
     */
    @PostMapping("/areas")
    public ResponseEntity<Object> insertAreasCategory(@Valid @RequestBody CategoryJusoVo param) throws JsonProcessingException {
        log.info("==================================================");
        log.info("제휴몰 주소 카테고리 등록 : {}", param);
        log.info("//================================================");
        CategoryJusoDto dtl = new CategoryJusoDto();

        if(!param.getJusoCode().isEmpty() & !param.getDisplayName().isEmpty()){
            dtl = categoryService.insertAreasCategory(param);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        PongiftApiResponse result = new PongiftApiResponse();
        result.setData(dtl);
        log.info("response : {}", JsonUtils.toJson(result));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * 주소 카테고리 업데이트
     *
     */
    @PutMapping("/areas")
    public ResponseEntity<Object> updateAreasCategory(@Valid @RequestBody CategoryJusoVo param) throws JsonProcessingException {
        log.info("==================================================");
        log.info("제휴몰 주소 카테고리 업데이트 : {}", param);
        log.info("//================================================");
        long upd = 0;
        CategoryJusoDto dtl = new CategoryJusoDto();
        if(!param.getJusoCode().isEmpty() & !param.getDisplayName().isEmpty()){
            dtl = categoryService.updateAreasCategory(param);
        }else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        PongiftApiResponse result = new PongiftApiResponse();
        result.setData(dtl);
        log.info("response : {}", JsonUtils.toJson(result));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 주소 카테고리 삭제
     *
     */
    @DeleteMapping("/delete-area/{categoryOid}")
    public ResponseEntity<Object> deleteAreasCategory(@PathVariable("categoryOid") String categoryOid) throws JsonProcessingException {
        log.info("==================================================");
        log.info("제휴몰 주소 카테고리 삭제 : {}", categoryOid);
        log.info("//================================================");
        long data = 0;
        if(!StringUtils.isEmpty(categoryOid)) {
            data = categoryService.deleteAreasCategory(categoryOid);
        }else{
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        PongiftApiResponse result = new PongiftApiResponse();
        result.setData(data);
        log.info("response : {}", JsonUtils.toJson(result));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * 유통채널 카테고리(상품 등록 시 조회)
     * @return
     */
    @GetMapping("/channel-goods")
    public ResponseEntity<Object> goodsCategoryList() throws JsonProcessingException {
        log.info("==================================================");
        log.info("제휴몰 유통채널 카테고리 조회");
        log.info("//================================================");
        List<CategoryGoodsDto> result = categoryService.selectCategoryGoodsAll(null);
        if(result.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        log.info("categoryList : {}", JsonUtils.toJson(result.size()));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
