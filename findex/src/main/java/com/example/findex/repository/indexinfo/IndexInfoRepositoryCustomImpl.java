package com.example.findex.repository.indexinfo;

import com.example.findex.dto.indexinfo.SortDirectionType;
import com.querydsl.core.BooleanBuilder;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.QIndexInfo;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class IndexInfoRepositoryCustomImpl implements IndexInfoRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<IndexInfo> findByFilters(
      String indexClassification,
      String indexName,
      Boolean favorite,
      Long idAfter,
      String cursor,
      String sortField,
      SortDirectionType sortDirectionType,
      Pageable pageable) {

    QIndexInfo i = QIndexInfo.indexInfo;
    BooleanBuilder whereClause = new BooleanBuilder();

    if (indexClassification != null && !indexClassification.isBlank()) {
      whereClause.and(i.indexClassification.containsIgnoreCase(indexClassification));
    }
    if (indexName != null && !indexName.isBlank()) {
      whereClause.and(i.indexName.containsIgnoreCase(indexName));
    }
    if (favorite != null) {
      whereClause.and(i.favorite.eq(favorite));
    }
    if (idAfter != null) {
      whereClause.and(i.id.gt(idAfter));
    }

    // 정렬 처리 (sortField에 따른 동적 정렬)
    if (sortField != null) {
      if (sortField.equals("indexClassification")) {
        whereClause.and(i.indexClassification.isNotNull());
      } else if (sortField.equals("indexName")) {
        whereClause.and(i.indexName.isNotNull());
      } else if (sortField.equals("employedItemsCount")) {
        whereClause.and(i.employedItemsCount.isNotNull());
      }
    }

    // 데이터를 페이지네이션 처리
    List<IndexInfo> results = queryFactory
        .selectFrom(i)
        .where(whereClause)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(
            // 첫 번째로 정렬 조건을 반영 (sortField가 null일 경우 기본 정렬)
            getOrderByClause(sortField, sortDirectionType, i)
        )
        .fetch();

    // 전체 데이터 수 조회
    long total = queryFactory
        .selectFrom(i)
        .where(whereClause)
        .fetchCount();

    // 마지막 요소 ID를 기준으로 페이지네이션 처리
    Long nextIdAfter = results.isEmpty() ? null : results.get(results.size() - 1).getId();
    boolean hasNext = results.size() == pageable.getPageSize();

    return new PageImpl<>(results, pageable, total);
  }

  // 정렬 조건을 동적으로 처리하는 메서드
  private OrderSpecifier<?> getOrderByClause(String sortField, SortDirectionType sortDirectionType, QIndexInfo i) {
    if (sortField == null || sortField.equals("indexClassification")) {
      return sortDirectionType == SortDirectionType.asc ? i.indexClassification.asc() : i.indexClassification.desc();
    } else if (sortField.equals("indexName")) {
      return sortDirectionType == SortDirectionType.asc ? i.indexName.asc() : i.indexName.desc();
    } else if (sortField.equals("employedItemsCount")) {
      return sortDirectionType == SortDirectionType.asc ? i.employedItemsCount.asc() : i.employedItemsCount.desc();
    }
    return i.id.asc();  // 기본 정렬은 id 기준으로 처리
  }
}