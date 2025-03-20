package com.example.findex.repository;

import com.example.findex.entity.IndexData;
import com.example.findex.entity.QIndexData;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
public class IndexDataRepositoryImpl implements IndexDataRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<IndexData> findIndexData(Long indexInfoId, LocalDate startDate, LocalDate endDate,
      Long idAfter, String sortField, Order sortOrder, Pageable pageable) {
    QIndexData indexData = QIndexData.indexData;
    BooleanBuilder builder = new BooleanBuilder();

    // indexInfoId 필터링 (완전 일치)
    if (indexInfoId != null) {
      builder.and(indexData.indexInfo.id.eq(indexInfoId));
    }

    // 날짜 범위 필터링
    if (startDate != null) {
      builder.and(indexData.baseDate.goe(startDate));
    }
    if (endDate != null) {
      builder.and(indexData.baseDate.loe(endDate));
    }

    // 이전 페이지 마지막 ID (커서 페이지네이션 적용)
    if (idAfter != null) {
      builder.and(indexData.id.lt(idAfter)); // 이전 ID보다 작은 데이터 조회
    }

    // QueryDSL Query 생성
    JPAQuery<IndexData> query = queryFactory
        .selectFrom(indexData)
        .where(builder);

    // 기본 정렬: ID 기준 내림차순
    OrderSpecifier<?> defaultSort = indexData.id.desc();
    OrderSpecifier<?> customSort = getSortOrder(indexData, sortField, sortOrder);

    if (customSort != null) {
      query.orderBy(customSort, defaultSort);
    } else {
      query.orderBy(defaultSort);
    }

    // 커서 페이지네이션 적용
    query.limit(pageable.getPageSize());

    return query.fetch();
  }
  private OrderSpecifier<?> getSortOrder(QIndexData indexData, String sortField, Order sortOrder) {
    switch (sortField) {
      case "baseDate":
        return sortOrder == Order.ASC ? indexData.baseDate.asc() : indexData.baseDate.desc();
      case "marketPrice":
        return sortOrder == Order.ASC ? indexData.marketPrice.asc() : indexData.marketPrice.desc();
      case "closingPrice":
        return sortOrder == Order.ASC ? indexData.closingPrice.asc() : indexData.closingPrice.desc();
      case "highPrice":
        return sortOrder == Order.ASC ? indexData.highPrice.asc() : indexData.highPrice.desc();
      case "lowPrice":
        return sortOrder == Order.ASC ? indexData.lowPrice.asc() : indexData.lowPrice.desc();
      case "versus":
        return sortOrder == Order.ASC ? indexData.versus.asc() : indexData.versus.desc();
      case "fluctuationRate":
        return sortOrder == Order.ASC ? indexData.fluctuationRate.asc() : indexData.fluctuationRate.desc();
      case "tradingQuantity":
        return sortOrder == Order.ASC ? indexData.tradingQuantity.asc() : indexData.tradingQuantity.desc();
      case "tradingPrice":
        return sortOrder == Order.ASC ? indexData.tradingPrice.asc() : indexData.tradingPrice.desc();
      case "marketTotalAmount":
        return sortOrder == Order.ASC ? indexData.marketTotalAmount.asc() : indexData.marketTotalAmount.desc();
      default:
        return null; // 허용되지 않은 필드는 정렬 적용 안 함
    }
  }
  /**
   * 전체 데이터 개수 조회
   */
  @Override
  public long countIndexData(Long indexInfoId, LocalDate startDate, LocalDate endDate) {
    QIndexData indexData = QIndexData.indexData;
    BooleanBuilder builder = new BooleanBuilder();

    if (indexInfoId != null) {
      builder.and(indexData.indexInfo.id.eq(indexInfoId));
    }
    if (startDate != null) {
      builder.and(indexData.baseDate.goe(startDate));
    }
    if (endDate != null) {
      builder.and(indexData.baseDate.loe(endDate));
    }

    return queryFactory
        .select(indexData.count())
        .from(indexData)
        .where(builder)
        .fetchOne();
  }


  @Transactional(readOnly = true)
  public List<IndexData> findByFilters(
      Long indexInfoId,
      LocalDate startDate,
      LocalDate endDate,
      String sortField,
      String sortDirection
  ) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<IndexData> query = cb.createQuery(IndexData.class);
    Root<IndexData> root = query.from(IndexData.class);

    List<Predicate> predicates = new ArrayList<>();

    if (indexInfoId != null) {
      predicates.add(cb.equal(root.get("indexInfo").get("id"), indexInfoId));
    }

    if (startDate != null) {
      predicates.add(cb.greaterThanOrEqualTo(root.get("baseDate"), startDate));
    }
    if (endDate != null) {
      predicates.add(cb.lessThanOrEqualTo(root.get("baseDate"), endDate));
    }

    query.where(predicates.toArray(new Predicate[0]));

    if (sortField != null && sortDirection != null) {
      Path<Object> orderField = root.get(sortField);
      if ("desc".equalsIgnoreCase(sortDirection)) {
        query.orderBy(cb.desc(orderField));
      } else {
        query.orderBy(cb.asc(orderField));
      }
    }

    return entityManager.createQuery(query).getResultList();
  }
}
