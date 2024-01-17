package com.pongift20.marketplace.backend.category.mapper;

import com.pongift20.marketplace.backend.category.model.vo.CategoryGoodsVo;
import com.pongift20.marketplace.backend.category.model.vo.CategoryJusoVo;
import com.pongift20.marketplace.backend.category.model.dto.CategoryGoodsDto;
import com.pongift20.marketplace.backend.category.model.dto.CategoryJusoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 상품 카테고리 관련 mapper
     */
    List<CategoryGoodsDto> selectCategoryGoods(String categoryId);
    List<CategoryGoodsDto> selectCategoryGoodsAll(String type);
    CategoryGoodsDto selectCategoryGoodsDtl(String categoryId);
    List<CategoryGoodsDto> searchGoodsCate(String goodsName);

    long insertGoodsCategory(CategoryGoodsVo param);

    long updateGoodsCategory(CategoryGoodsVo param);

    long deleteGoodsCategory(String param);


    /**
     * 지역 카테고리 관련 mapper
     */
    List<CategoryJusoDto> selectCategoryJuso(String addr1, String addr2);
    List<CategoryJusoDto> selectCategoryJusoAll();
    List<CategoryJusoDto> selectCategoryJusoList();
    CategoryJusoDto selectCategoryAreasDtl(String jusoCode);

    List<CategoryJusoDto> selectCategoryAreasInfoList(String jusoCode);

    long insertCategoryJuso(CategoryJusoVo param);

    long updateCategoryJuso(CategoryJusoVo param);

    long deleteCategoryJuso(String param);
}
