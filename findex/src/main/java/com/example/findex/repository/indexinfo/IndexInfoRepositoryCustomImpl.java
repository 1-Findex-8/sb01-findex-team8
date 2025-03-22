package com.example.findex.repository.indexinfo;

import com.example.findex.dto.indexinfo.SortDirectionType;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.QIndexInfo;
import com.example.findex.repository.indexinfo.IndexInfoRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
      int size) {

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

    // 정렬 조건 생성 (중복 방지)
    List<OrderSpecifier<?>> orderSpecifiers = getOrderByClause(sortField, sortDirectionType, i);

    // 커서 조건 추가
    applyCursorCondition(whereClause, cursor, sortField, sortDirectionType, i);

    // 전체 개수 조회 (한 번만 수행)
    Long totalElements = queryFactory
        .select(i.count())
        .from(i)
        .where(whereClause)
        .fetchOne();

    // 데이터 조회 (커서 기반 페이징 적용)
    List<IndexInfo> results = queryFactory
        .selectFrom(i)
        .where(whereClause)
        .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0])) // 정렬 적용
        .limit(size)
        .fetch();

    return new PageImpl<>(results, PageRequest.of(0, size), totalElements);
  }

  // 정렬 기준 설정 (id 추가)
  private List<OrderSpecifier<?>> getOrderByClause(String sortField, SortDirectionType sortDirectionType, QIndexInfo i) {
    List<OrderSpecifier<?>> orders = new ArrayList<>();

    if ("indexClassification".equals(sortField)) {
      orders.add(sortDirectionType == SortDirectionType.asc ? i.indexClassification.asc() : i.indexClassification.desc());
    } else if ("indexName".equals(sortField)) {
      orders.add(sortDirectionType == SortDirectionType.asc ? i.indexName.asc() : i.indexName.desc());
    } else if ("employedItemsCount".equals(sortField)) {
      orders.add(sortDirectionType == SortDirectionType.asc ? i.employedItemsCount.asc() : i.employedItemsCount.desc());
    }

    // ID 추가 (중복 방지)
    orders.add(sortDirectionType == SortDirectionType.asc ? i.id.asc() : i.id.desc());

    return orders;
  }

  // 커서 조건 추가
  private void applyCursorCondition(BooleanBuilder whereClause, String cursor, String sortField, SortDirectionType sortDirectionType, QIndexInfo i) {
    if (cursor == null) return;

    String[] cursorParts = cursor.split(":"); // "정렬필드값:ID" 형식으로 전달
    String cursorValue = cursorParts[0];
    Long cursorId = Long.parseLong(cursorParts[1]);

    if ("indexClassification".equals(sortField)) {
      whereClause.and(
          sortDirectionType == SortDirectionType.asc
              ? i.indexClassification.gt(cursorValue).or(i.indexClassification.eq(cursorValue).and(i.id.gt(cursorId)))
              : i.indexClassification.lt(cursorValue).or(i.indexClassification.eq(cursorValue).and(i.id.lt(cursorId)))
      );
    } else if ("indexName".equals(sortField)) {
      whereClause.and(
          sortDirectionType == SortDirectionType.asc
              ? i.indexName.gt(cursorValue).or(i.indexName.eq(cursorValue).and(i.id.gt(cursorId)))
              : i.indexName.lt(cursorValue).or(i.indexName.eq(cursorValue).and(i.id.lt(cursorId)))
      );
    } else if ("employedItemsCount".equals(sortField)) {
      whereClause.and(
          sortDirectionType == SortDirectionType.asc
              ? i.employedItemsCount.gt(Integer.parseInt(cursorValue)).or(i.employedItemsCount.eq(Integer.parseInt(cursorValue)).and(i.id.gt(cursorId)))
              : i.employedItemsCount.lt(Integer.parseInt(cursorValue)).or(i.employedItemsCount.eq(Integer.parseInt(cursorValue)).and(i.id.lt(cursorId)))
      );
    } else {
      whereClause.and(
          sortDirectionType == SortDirectionType.asc ? i.id.gt(cursorId) : i.id.lt(cursorId)
      );
    }
  }
}