package com.pongift20.marketplace.backend.goods.model.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pongift20.marketplace.backend.utils.JsonUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 페이징 NextToken
 */
@Slf4j
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NextToken {
    private String keyword; // 검색 키워드
    private String category; // 검색 카테고리
    private String addr1;
    private String addr2;
    private String addr3;
    private String sortBy; // 정렬 기준 필드
    private String sortOrder; // 정렬 순서
    private String lastValue; // (페이징) 정렬 기준 필드 마지막 값
    private long lastId; // (페이징) 마지막 pk

    public String toJsonString() {
        try {
            return JsonUtils.toJson(this);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NextToken nextToken = (NextToken) o;
        return lastId == nextToken.lastId &&
                Objects.equals(keyword, nextToken.keyword) &&
                Objects.equals(category, nextToken.category) &&
                Objects.equals(addr1, nextToken.addr1) &&
                Objects.equals(addr2, nextToken.addr2) &&
                Objects.equals(addr3, nextToken.addr3) &&
                Objects.equals(sortBy, nextToken.sortBy) &&
                Objects.equals(sortOrder, nextToken.sortOrder) &&
                Objects.equals(lastValue, nextToken.lastValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword, category, addr1, addr2, addr3, sortBy, sortOrder, lastValue, lastId);
    }
}
