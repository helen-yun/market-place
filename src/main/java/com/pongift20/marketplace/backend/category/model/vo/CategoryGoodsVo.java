package com.pongift20.marketplace.backend.category.model.vo;

import com.pongift20.marketplace.backend.category.model.dto.CategoryGoodsDto;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
public class CategoryGoodsVo {
    @NotNull
    private String categoryId;
    private String parentId;
    private String categoryName;
    private String categoryOid;
    private Boolean enabled;


    @Getter
    @Builder
    public static class list{
        private List<CategoryGoodsDto> list;
        private List<CategoryGoodsDto> data;
    }
}
