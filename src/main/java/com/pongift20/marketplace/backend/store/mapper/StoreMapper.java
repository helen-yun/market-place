package com.pongift20.marketplace.backend.store.mapper;

import com.pongift20.marketplace.backend.store.model.vo.StoreVo;
import com.pongift20.marketplace.backend.store.model.dto.StoreDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreMapper {
    StoreDto selectStore(long storeSeq);

    void createStore(StoreVo param);

    long updateStore(StoreVo param);
}
