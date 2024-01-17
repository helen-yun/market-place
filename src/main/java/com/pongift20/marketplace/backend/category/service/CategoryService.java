package com.pongift20.marketplace.backend.category.service;

import com.pongift20.marketplace.backend.category.mapper.CategoryMapper;
import com.pongift20.marketplace.backend.category.model.vo.CategoryGoodsVo;
import com.pongift20.marketplace.backend.category.model.vo.CategoryJusoVo;
import com.pongift20.marketplace.backend.category.model.dto.CategoryGoodsDto;
import com.pongift20.marketplace.backend.category.model.dto.CategoryJusoDto;
import com.pongift20.marketplace.backend.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryMapper categoryMapper;

    /**
     * 상품 카테고리 service
     */
    public List<CategoryGoodsDto> selectCategoryGoods(String categoryId){
        return categoryMapper.selectCategoryGoods(categoryId);
    }

    public List<CategoryGoodsDto> selectCategoryGoodsAll(String type){
        return categoryMapper.selectCategoryGoodsAll(type);
    }

    public CategoryGoodsDto selectCategoryGoodsDtl(String categoryId){
        return categoryMapper.selectCategoryGoodsDtl(categoryId);
    }

    public List<CategoryGoodsDto> searchGoodsCate(String goodsName){
        return categoryMapper.searchGoodsCate(goodsName);
    }

    @Transactional
    public CategoryGoodsDto insertGoodsCategory(CategoryGoodsVo param) {
        try{
            long result = categoryMapper.insertGoodsCategory(param);
            if(result > 0){
                return selectCategoryGoodsDtl(param.getCategoryId());
            }else{
                log.error("상품 카테고리 등록 오류 : {}", JsonUtils.toJson(param));
                throw new RuntimeException();
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException();
        }

    }

    @Transactional
    public CategoryGoodsDto updateGoodsCategory(CategoryGoodsVo param){
        try{
            long result = categoryMapper.updateGoodsCategory(param);
            if(result > 0){
                return selectCategoryGoodsDtl(param.getCategoryId());
            }else{
                throw new RuntimeException();
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException();
        }

    }

    @Transactional
    public long deleteGoodsCategory(String param){
        try{
            long result = categoryMapper.deleteGoodsCategory(param);
            if(result <= 0){
                log.error("상품 카테고리 삭제 오류 : {}", param);
                throw new RuntimeException();
            }
            return result;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }


    /**
     * 지역 카테고리 service
     */
    public List<CategoryJusoDto> selectCategoryJuso(String addr1, String addr2){
        return categoryMapper.selectCategoryJuso(addr1, addr2);
    }

    public List<CategoryJusoDto> selectCategoryJusoAll(){
        return categoryMapper.selectCategoryJusoAll();
    }

    public CategoryJusoDto selectCategoryAreasDtl(String jusoCode){
        return categoryMapper.selectCategoryAreasDtl(jusoCode);
    }

    public List<CategoryJusoDto> selectCategoryAreasInfoList(String jusoCode){
        return categoryMapper.selectCategoryAreasInfoList(jusoCode);
    }

    public List<CategoryJusoDto> selectCategoryJusoList(){
        return categoryMapper.selectCategoryJusoList();
    }

    @Transactional
    public CategoryJusoDto insertAreasCategory(CategoryJusoVo param) {
        try{
            CategoryJusoDto vo = new CategoryJusoDto();
            if(!param.getParentId().isEmpty()){
                vo = selectCategoryAreasDtl(param.getCategoryOid());
                if(param.getDepth() == 1){
                    param.setAddr1(vo.getAddr1());
                    param.setAddr2(param.getDisplayName());
                    param.setAddr3(null);
                }else if(param.getDepth() == 2){
                    param.setAddr1(vo.getAddr1());
                    param.setAddr2(vo.getAddr2());
                    param.setAddr3(param.getDisplayName());
                }
            }else{
                param.setAddr1(param.getDisplayName());
                param.setAddr2(null);
                param.setAddr3(null);
            }
            long result = categoryMapper.insertCategoryJuso(param);
            if(result > 0){
                return selectCategoryAreasDtl(param.getJusoCode());
            }else{
                log.error("지역 카테고리 등록 오류 : {}", JsonUtils.toJson(param));
                throw new RuntimeException();
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException();
        }

    }

    @Transactional
    public CategoryJusoDto updateAreasCategory(CategoryJusoVo param){
        try{
            CategoryJusoDto vo = new CategoryJusoDto();
            if(!param.getParentId().isEmpty()){
                vo = selectCategoryAreasDtl(param.getCategoryOid());
                if(param.getDepth() == 2){
                    param.setAddr1(vo.getAddr1());
                    param.setAddr2(param.getDisplayName());
                    param.setAddr3(null);
                }else if(param.getDepth() == 3){
                    param.setAddr1(vo.getAddr1());
                    param.setAddr2(vo.getAddr2());
                    param.setAddr3(param.getDisplayName());
                }
            }else{
                param.setAddr1(param.getDisplayName());
                param.setAddr2(null);
                param.setAddr3(null);
            }
            long result = categoryMapper.updateCategoryJuso(param);
            if(result > 0){
                return selectCategoryAreasDtl(param.getJusoCode());
            }else{
                log.error("지역 카테고리 수정 오류 : {}", JsonUtils.toJson(param));
                throw new RuntimeException();
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException();
        }

    }

    @Transactional
    public long deleteAreasCategory(String param){
        try{
            long result = categoryMapper.deleteCategoryJuso(param);
            if(result <= 0) {
                throw new RuntimeException();
            }
            return result;
        }catch (Exception e){
            log.error("지역 카테고리 삭제 오류 : {}", e.getMessage());
            throw new RuntimeException();
        }
    }
}
