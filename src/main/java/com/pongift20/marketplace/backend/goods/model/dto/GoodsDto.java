package com.pongift20.marketplace.backend.goods.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("goods")
public class GoodsDto {
    private long goodsSeq;
    private long storeSeq;
    private String goodsName;
    private String goodsThumbnailSrc;
    private String goodsInfoSrc;
    private String goodsStoreInfoSrc;
    private BigDecimal goodsSalePrice;
    private BigDecimal goodsRetailPrice;
    private int goodsStockCount;
    private String goodsHtml;
    private boolean enabled;

    //매장명
    private String storeName;
    //서비스명
    private String serviceName;

    //상품 전체 주소
    private String goodsJuso;

    @JsonIgnore
    private Date createAt;
    @JsonIgnore
    private Date modifiedAt;

}
