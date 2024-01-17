package com.pongift20.marketplace.backend.category.model.vo;

import com.pongift20.marketplace.backend.category.model.dto.CategoryJusoDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
public class CategoryJusoVo {
    @NotNull
    private String jusoCode;
    private String addr1;
    private String addr2;
    private String addr3;
    private String displayName;
    private String parentId;
    private String categoryOid;
    private int depth;
    private Boolean enabled;

    @Getter
    @Builder
    public static class list{
        private List<CategoryJusoDto> list;
    }

    @Getter
    @Data
    public static class Juso{
        private String jusoCode;
        private String addr1;
        private String addr2;
        private String add3;
        private String displayName;
        private String parentId;
        private Boolean enabled;
    }
}
