package com.pongift20.marketplace.backend.store.service;

import com.pongift20.marketplace.backend.store.model.vo.StoreVo;
import com.pongift20.marketplace.backend.store.mapper.StoreMapper;
import com.pongift20.marketplace.backend.store.model.dto.StoreDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreService {
    private final StoreMapper storeMapper;

    /**
     * 매장 조회
     * @param storeSeq 매장 pk
     * @return
     */

    public StoreDto selectStore(long storeSeq) {
        StoreDto result;
        try{
            result = storeMapper.selectStore(storeSeq);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException();
        }
        return result;
    }

    /**
     * 매장 등록
     * @param param
     * @return
     */
    @Transactional
    public StoreDto insertStore(StoreVo param) {
        StoreDto result;
        try{
            storeMapper.createStore(param);
            long seq = param.getStoreSeq();
            if(seq < 0) throw new RuntimeException();

            result = storeMapper.selectStore(seq);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException();
        }
        return result;
    }

    /**
     * 매장 수정
     * @param param
     * @return
     */
    @Transactional
    public StoreDto updateStore(StoreVo param) {
        StoreDto result;
        try{
            long upd = storeMapper.updateStore(param);
            if(upd < 0) throw new RuntimeException();

            result = storeMapper.selectStore(param.getStoreSeq());
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException();
        }
        return result;
    }
}
