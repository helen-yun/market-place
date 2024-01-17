package com.pongift20.marketplace.backend.category.controller;

import com.pongift20.marketplace.backend.category.model.vo.CategoryGoodsVo;
import com.pongift20.marketplace.backend.category.model.vo.CategoryJusoVo;
import com.pongift20.marketplace.backend.category.model.dto.CategoryGoodsDto;
import com.pongift20.marketplace.backend.category.model.dto.CategoryJusoDto;
import com.pongift20.marketplace.backend.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 상품 카테고리 조회
     *
     */
    @GetMapping("/goods")
    public ResponseEntity<CategoryGoodsVo.list> categoryGoodsList(@RequestParam(value = "categoryId", required = false) String categoryId){
        List<CategoryGoodsDto> list = categoryService.selectCategoryGoods(categoryId);
        if(list.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        CategoryGoodsVo.list result = CategoryGoodsVo.list.builder()
                .list(list)
                .build();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 검색 조건에 따른 상품 카테고리 조회
     * @param goodsName 상품 검색어
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<CategoryGoodsVo.list> searchGoodsCate(@RequestParam(value="goodsName", required = false) String goodsName){
        List<CategoryGoodsDto> list = categoryService.searchGoodsCate(goodsName);
        if(list.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        CategoryGoodsVo.list result = CategoryGoodsVo.list.builder()
                .list(list)
                .build();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 주소 카테고리 뎁스별 조회
     *
     * @param addr1 depth1 지역명(파라미터 없을 경우 depth별 전체값 조회)
     * @param addr2 depth3 조회 시 필수값
     *
     * */
    @GetMapping("/area")
    public ResponseEntity<CategoryJusoVo.list> categoryJuso(@RequestParam(required = false, value="addr1") String addr1,
                                                            @RequestParam(required = false, value="addr2") String addr2){
        List<CategoryJusoDto> list = categoryService.selectCategoryJuso(addr1, addr2);
        if(list.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        CategoryJusoVo.list result = CategoryJusoVo.list.builder()
                .list(list)
                .build();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 주소 카테고리 전체 조회
     *
     * */
    @GetMapping("/areaAll")
    public ResponseEntity<CategoryJusoVo.list> categoryJusoAll(){
        List<CategoryJusoDto> list = categoryService.selectCategoryJusoAll();
        if(list.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        CategoryJusoVo.list result = CategoryJusoVo.list.builder()
                .list(list)
                .build();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}