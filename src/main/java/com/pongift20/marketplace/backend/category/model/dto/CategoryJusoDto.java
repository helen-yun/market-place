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
@Alias("categoryJuso")
public class CategoryJusoDto {
    private String jusoCode;
    private String addr1;
    private String addr2;
    private String addr3;
    private String displayName;
    private String parentId;
    private int depth;
    private Boolean enabled;
}
