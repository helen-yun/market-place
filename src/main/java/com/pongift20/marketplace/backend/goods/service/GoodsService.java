package com.pongift20.marketplace.backend.goods.service;

import com.pongift20.marketplace.backend.internal.code.ExhibitSt;
import com.pongift20.marketplace.backend.internal.goods.dto.NewGoodsDto;
import com.pongift20.marketplace.backend.goods.model.vo.NextToken;
import com.pongift20.marketplace.backend.goods.model.dto.GoodsDto;
import com.pongift20.marketplace.backend.goods.mapper.GoodsMapper;
import com.pongift20.marketplace.backend.utils.AES256Util;
import com.pongift20.marketplace.backend.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsService {
    private final int PAGE_SIZE = 31;
    private final GoodsMapper goodsMapper;

    /**
     * NextToken 기준 상품 목록 조회
     *
     * @param nextToken 다음페이지 토큰
     * @return List<GoodsVo>
     */
    public List<GoodsDto> selectGoodsListByNextToken(NextToken nextToken) {
        return goodsMapper.selectGoodsList(nextToken.getSortBy()
                , nextToken.getSortOrder()
                , nextToken.getKeyword()
                , nextToken.getCategory()
                , nextToken.getAddr1()
                , nextToken.getAddr2()
                , nextToken.getAddr3()
                , nextToken.getLastValue()
                , nextToken.getLastId()
                , PAGE_SIZE);
    }

    /**
     * 상품 목록 조회 (init, nextToken이 없는 경우)
     *
     * @param sortBy    정렬 기준 필드
     * @param sortOrder 정렬 순서 (desc, asc)
     * @param keyword   검색어
     * @param category  카테고리 (categoryId, =BAH)
     * @param addr1     1차주소 (=서울)
     * @param addr2     2차주소 (=종로구)
     * @param addr3     3차주소 (=청운동)
     * @return List<GoodsVo>
     */
    public List<GoodsDto> selectGoodsList(String sortBy, String sortOrder, String keyword, String category, String addr1, String addr2, String addr3) {
        return goodsMapper.selectGoodsList(sortBy
                , sortOrder
                , keyword
                , category
                , addr1
                , addr2
                , addr3
                , null
                , 0
                , PAGE_SIZE);
    }

    /**
     * 상품 갯수 카운트
     *
     * @param keyword  검색어
     * @param category 카테고리 (categoryId, =BAH)
     * @param addr1    1차주소 (=서울)
     * @param addr2    2차주소 (=종로구)
     * @param addr3    3차주소 (=청운동)
     * @return long
     */
    public long countGoodsList(String keyword, String category, String addr1, String addr2, String addr3) {
        return goodsMapper.countGoodsList(keyword, category, addr1, addr2, addr3);
    }

    /**
     * 상품 상세 조회
     *
     * @param goodsSeq 상품 PK
     * @return GoodsVo
     */
    public GoodsDto selectGoodsDetail(long goodsSeq) {
        return goodsMapper.selectGoodsDetail(goodsSeq, 0);
    }

    /**
     * 상품조회(GateWay)
     *
     * @param goodsId
     * @return
     */
    public NewGoodsDto selectGoods(String goodsId) {
        try {
            //상품 조회
            GoodsDto goods = goodsMapper.selectGoodsDetailFromGw(Long.parseLong(goodsId));
            return NewGoodsDto.builder()
                    .exhibitSt(goods.isEnabled() ? ExhibitSt.EXPOSURE.getCode() : ExhibitSt.UNEXPOSED.getCode())
                    .saleEndDate("20301231")
                    .goodsSeq(goods.getGoodsSeq())
                    .storeSeq(goods.getStoreSeq())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 상품 등록(GateWay)
     *
     * @param newGoodsDto
     */
    @Transactional
    public void createGoods(NewGoodsDto newGoodsDto) {
        try {
            //매장 존재 여부 조회
            long seq = goodsMapper.checkStore(newGoodsDto);
            if(seq <= 0){
                throw new RuntimeException();
            }

            //상품 등록
            goodsMapper.insertGoods(newGoodsDto);

            //카테고리 등록
            goodsMapper.insertGoodsCategory(newGoodsDto.getGoodsSeq(), newGoodsDto.getCategories().get(0));

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * 상품 수정(GateWay)
     *
     * @param newGoodsDto
     */
    @Transactional
    public long updateGoods(NewGoodsDto newGoodsDto) {
        try {
            //상품 업데이트
            newGoodsDto.setEnabled(newGoodsDto.getExhibitSt().equals(ExhibitSt.EXPOSURE.getCode()));
            goodsMapper.updateGoods(newGoodsDto);

            //카테고리 업데이트
            return goodsMapper.updateGoodsCategory(Long.parseLong(newGoodsDto.getGoodsId()), newGoodsDto.getCategories().get(0));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * 상품 전시상태 수정(GateWay)
     *
     * @param goodsId
     */
    @Transactional
    public long updateGoodsExhibits(String goodsId) {
        try {
            //상품 업데이트
            return goodsMapper.updateGoodsExhibits(goodsId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }


    /**
     * NextToken 인코딩
     *
     * @param nextToken 페이징 NextToken
     * @return NextToken (다음페이지가 없는 경우 공백)
     */
    public String encodeNextToken(NextToken nextToken) {
        if (nextToken == null) return "";

        try {
            AES256Util AES256Util = new AES256Util("UTF-8");
            return AES256Util.aesEncode(nextToken.toJsonString());
        } catch (Exception e) {
            log.error(">>>>> encodeNextToken 예외 발생", e);
            return null;
        }
    }

    /**
     * NextToken 디코딩
     *
     * @param encodeNextToken 인코딩된 NextToken 문자열
     * @return NextToken
     */
    public NextToken decodeNextToken(String encodeNextToken) {
        try {
            AES256Util AES256Util = new AES256Util("UTF-8");
            return JsonUtils.fromJson(AES256Util.aesDecode(encodeNextToken), NextToken.class);
        } catch (Exception e) {
            log.error(">>>>> decodeNextToken 예외 발생", e);
            return null;
        }
    }

    /**
     * NextToken 생성
     *
     * @param goodsDtoList 상품 조회 결과
     * @param keyword     검색어
     * @param category    카테고리 (categoryId, =BAH)
     * @param addr1       1차주소 (=서울)
     * @param addr2       2차주소 (=종로구)
     * @param addr3       3차주소 (=청운동)
     * @param sortBy      정렬 기준 필드
     * @param sortOrder   정렬 순서 (desc, asc)
     * @return NextToken (다음페이지가 없는 경우 null)
     */
    public NextToken createNextToken(List<GoodsDto> goodsDtoList, String keyword, String category, String addr1, String addr2, String addr3, String sortBy, String sortOrder) {
        if (goodsDtoList.size() <= PAGE_SIZE - 1) {
            return null;
        }
        GoodsDto lastGoods;
        if (goodsDtoList.size() == PAGE_SIZE) {
            lastGoods = goodsDtoList.get(PAGE_SIZE - 2);
        } else {
            lastGoods = goodsDtoList.get(goodsDtoList.size() - 1);
        }

        String lastValue;
        if (sortBy.equals("salePrice")) {
            lastValue = String.valueOf(lastGoods.getGoodsSalePrice());
        } else { /* if (sortBy.equals("goodsSeq")) */
            lastValue = String.valueOf(lastGoods.getGoodsSeq());
        }
        return NextToken.builder()
                .keyword(keyword)
                .category(category)
                .addr1(addr1)
                .addr2(addr2)
                .addr3(addr3)
                .lastId(lastGoods.getGoodsSeq())
                .lastValue(lastValue)
                .sortBy(sortBy)
                .sortOrder(sortOrder)
                .build();
    }
}
