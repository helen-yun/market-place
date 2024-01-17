package com.pongift20.marketplace.backend.category.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("categoryGoods")
public class CategoryGoodsDto {
    private String categoryId;
    private String parentId;
    private String categoryName;
    private int depth;
    private Boolean enabled;
}
