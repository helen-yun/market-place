package com.pongift20.marketplace.backend.store.model.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("store")
public class StoreDto {
    private long storeSeq; //pk
    private String storeName; //매장이름
    private long providerSeq; //공급사 pk
    private String jusoCode; //카테고리 주소 코드
    private String storeId; //어드민 매장 id
}
