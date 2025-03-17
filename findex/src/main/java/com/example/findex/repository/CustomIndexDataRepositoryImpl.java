package com.example.findex.repository;

import com.example.findex.dto.indexdata.data.IndexDataDto;
import com.example.findex.dto.indexdata.response.CursorPageResponseIndexDataDto;
import com.example.findex.entity.IndexData;
import com.example.findex.entity.QIndexData;
import com.example.findex.entity.SourceType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomIndexDataRepositoryImpl implements CustomIndexDataRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public CursorPageResponseIndexDataDto getIndexDataList(long indexInfold, LocalDate startDate, LocalDate endDate, long idAfter, String cursor,String sortField, String sortDirection, int size) {
    QIndexData indexData = QIndexData.indexData;

    // BooleanBuilder로 필터링 조건 작성
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(indexData.indexInfo.id.eq(indexInfold)); // 지수 정보 ID 필터링
    builder.and(indexData.baseDate.between(startDate, endDate)); // 날짜 범위 필터링

    if (idAfter > 0) {
      builder.and(indexData.id.gt(idAfter)); // 커서 기반 페이지네이션을 위한 ID 필터링
    }

    // 쿼리 실행
    List<IndexData> results = jpaQueryFactory
        .selectFrom(indexData)
        .where(builder)
        .orderBy(getSortOrder(sortField, sortDirection)) // 동적 정렬
        .limit(size) // 페이지 크기 제한
        .fetch(); // 쿼리 실행

    // DTO 변환 및 결과 반환
    List<IndexDataDto> content = mapToIndexDataDto(results);
    boolean hasNext = results.size() == size; // 다음 페이지가 있는지 여부 판단
    String nextCursor = hasNext ? results.get(results.size() - 1).getId().toString() : null; // 커서 (다음 페이지 시작점)
    Long totalElements = getTotalElements(indexInfold, startDate, endDate); // 총 요소 수

    return new CursorPageResponseIndexDataDto(content, nextCursor, results.isEmpty() ? null : results.get(results.size() - 1).getId(), size, totalElements, hasNext);
  }

  private OrderSpecifier<?> getSortOrder(String sortField, String sortDirection) {
    QIndexData indexData = QIndexData.indexData;

    switch (sortField) {
      case "baseDate":
        return "asc".equals(sortDirection) ? indexData.baseDate.asc() : indexData.baseDate.desc();
      case "marketPrice":
        return "asc".equals(sortDirection) ? indexData.marketPrice.asc() : indexData.marketPrice.desc();
      case "closingPrice":
        return "asc".equals(sortDirection) ? indexData.closingPrice.asc() : indexData.closingPrice.desc();
      case "highPrice":
        return "asc".equals(sortDirection) ? indexData.highPrice.asc() : indexData.highPrice.desc();
      case "lowPrice":
        return "asc".equals(sortDirection) ? indexData.lowPrice.asc() : indexData.lowPrice.desc();
      case "versus":
        return "asc".equals(sortDirection) ? indexData.versus.asc() : indexData.versus.desc();
      case "fluctuationRate":
        return "asc".equals(sortDirection) ? indexData.fluctuationRate.asc() : indexData.fluctuationRate.desc();
      case "tradingQuantity":
        return "asc".equals(sortDirection) ? indexData.tradingQuantity.asc() : indexData.tradingQuantity.desc();
      case "tradingPrice":
        return "asc".equals(sortDirection) ? indexData.tradingPrice.asc() : indexData.tradingPrice.desc();
      case "marketTotalAmount":
        return "asc".equals(sortDirection) ? indexData.marketTotalAmount.asc() : indexData.marketTotalAmount.desc();
      default:
        return indexData.baseDate.asc();  // 기본 정렬: baseDate 기준 오름차순
    }
  }

  private List<IndexDataDto> mapToIndexDataDto(List<IndexData> results) {
    // IndexData 엔티티를 IndexDataDto로 변환하는 메서드
    return results.stream()
        .map(indexData -> new IndexDataDto(
            indexData.getId(),
            indexData.getIndexInfo().getId(),
            indexData.getBaseDate(),
            SourceType.USER,
            indexData.getMarketPrice(),
            indexData.getClosingPrice(),
            indexData.getHighPrice(),
            indexData.getLowPrice(),
            indexData.getVersus(),
            indexData.getFluctuationRate(),
            indexData.getTradingQuantity(),
            indexData.getTradingPrice(),
            indexData.getMarketTotalAmount()
        ))
        .toList();
  }

  private Long getTotalElements(long indexInfold, LocalDate startDate, LocalDate endDate) {
    // 총 요소 수를 구하는 쿼리
    QIndexData indexData = QIndexData.indexData;

    return jpaQueryFactory.select(indexData.count())
        .from(indexData)
        .where(indexData.indexInfo.id.eq(indexInfold))
        .where(indexData.baseDate.between(startDate, endDate))
        .fetchOne();
  }
}
