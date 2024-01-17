package com.pongift20.marketplace.backend.store.model.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StoreVo {
    private long storeSeq; //pk
    private String storeName; //매장이름
    private String serviceName; //어드민 서비스명
    private long providerSeq; //공급사 pk
    private String jusoCode; //카테고리 주소 코드
}
