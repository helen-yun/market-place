package com.pongift20.marketplace.backend.goods.mapper;

import com.pongift20.marketplace.backend.internal.goods.dto.NewGoodsDto;
import com.pongift20.marketplace.backend.goods.model.dto.GoodsDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {
    /**
     * 상품 목록 조회
     *
     * @param sortBy    정렬 기준
     * @param sortOrder 정렬 순서
     * @param keyword   검색어
     * @param lastValue 정렬 기준 마지막 값 (페이징)
     * @param lastId    마지막 pk (페이징)
     * @param pageSize  페이징 사이즈
     * @param category 카테고리Id
     * @param addr1           1차주소
     * @param addr2           2차주소
     * @param addr3           3차주소
     * @return List<GoodsVo>
     */
    List<GoodsDto> selectGoodsList(String sortBy, String sortOrder, String keyword, String category, String addr1, String addr2, String addr3, String lastValue, long lastId, int pageSize);

    /**
     * 상품 갯수 조회
     *
     * @param keyword 검색어
     * @return long
     */
    long countGoodsList(String keyword, String category, String addr1, String addr2, String addr3); //TODO: WHERE 조건절 추가 필요

    /**
     * 상품 상세 조회
     *
     * @param goodsSeq 상품 PK
     * @return GoodsVo
     */
    GoodsDto selectGoodsDetail(long goodsSeq, long goodsPriceSeq);

    /**
     * 매장 존재 여부
     * @param dto
     * @return
     */
    long checkStore(NewGoodsDto dto);

    /**
     * 상품추가
     * @param dto
     * @return
     */
    void insertGoods(NewGoodsDto dto);

    /**
     * 상품-카테고리 추가
     * @param goodsSeq 상품pk
     * @param category 카테고리코드
     */
    void insertGoodsCategory(long goodsSeq, String category);

    /**
     * 상품 업데이트
     * @param dto
     */
    void updateGoods(NewGoodsDto dto);

    /**
     * 상품 카테고리 업데이트
     * @param goodsId
     * @param category
     */
    long updateGoodsCategory(long goodsId, String category);

    /**
     * 상품조회
     * @param goodsId
     * @return
     */
    GoodsDto selectGoodsDetailFromGw(long goodsId);

    /**
     * 상품 전시상태 업데이트
     *
     */
    long updateGoodsExhibits(String goodsId);
}
