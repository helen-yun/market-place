package com.pongift20.marketplace.backend.internal.goods.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
public class NewGoodsDto {
    private long goodsSeq;
    private long storeSeq;
    private String goodsId;
    @Size(min = 1, max = 10)
    private List<String> categories;
    @Length(max = 50)
    private String goodsNm;
    @Min(0)
    private Integer stockCnt;
    @Min(0)
    @Max(Integer.MAX_VALUE - 1)
    private Integer salePrice;
    @Min(0)
    @Max(Integer.MAX_VALUE - 1)
    private Integer retailPrice;
    @Min(0)
    @Max(Integer.MAX_VALUE - 1)
    private Integer discountPrice;
    private String exhibitSt;
    private String expiryGb;
    private String html;
    private String storeName;
    private String storeId;
    //상품 대표 이미지
    private String goodsImage;
    //상품 상세 이미지
    private String infoUrl;
    //매장 상세 이미지
    private String storeUrl;
    //전시기간
    private String saleEndDate;
    private Boolean enabled;
}
