package com.example.findex.repository;

import com.example.findex.entity.IndexData;
import com.example.findex.entity.QIndexData;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
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

    if (idAfter != null) {
      builder.and(sortOrder == Order.ASC
          ? indexData.id.gt(idAfter)  // ASC 정렬이면 idAfter보다 큰 값 조회
          : indexData.id.lt(idAfter)  // DESC 정렬이면 idAfter보다 작은 값 조회
      );
    }

    // QueryDSL Query 생성
    JPAQuery<IndexData> query = queryFactory
        .selectFrom(indexData)
        .where(builder);

    // 단 하나의 정렬 조건만 적용
    OrderSpecifier<?> selectedSort = getSortOrder(indexData, sortField, sortOrder);
    if (selectedSort != null) {
      query.orderBy(selectedSort, sortOrder == Order.ASC ? indexData.id.asc() : indexData.id.desc());
    } else {
      query.orderBy(indexData.id.desc()); // 기본 정렬
    }

    // 커서 페이지네이션 적용
    query.limit(pageable.getPageSize());

    return query.fetch();
  }
  private OrderSpecifier<?> getSortOrder(QIndexData indexData, String sortField, Order sortOrder) {
    if (sortField == null) {
      return null; // 정렬 필드가 없으면 기본 정렬 적용
    }

    boolean isAscending = (sortOrder == Order.ASC);

    switch (sortField) {
      case "baseDate":
        return isAscending ? indexData.baseDate.asc() : indexData.baseDate.desc();
      case "marketPrice":
        return isAscending ? indexData.marketPrice.asc() : indexData.marketPrice.desc();
      case "closingPrice":
        return isAscending ? indexData.closingPrice.asc() : indexData.closingPrice.desc();
      case "highPrice":
        return isAscending ? indexData.highPrice.asc() : indexData.highPrice.desc();
      case "lowPrice":
        return isAscending ? indexData.lowPrice.asc() : indexData.lowPrice.desc();
      case "versus":
        return isAscending ? indexData.versus.asc() : indexData.versus.desc();
      case "fluctuationRate":
        return isAscending ? indexData.fluctuationRate.asc() : indexData.fluctuationRate.desc();
      case "tradingQuantity":
        return isAscending ? indexData.tradingQuantity.asc() : indexData.tradingQuantity.desc();
      case "tradingPrice":
        return isAscending ? indexData.tradingPrice.asc() : indexData.tradingPrice.desc();
      case "marketTotalAmount":
        return isAscending ? indexData.marketTotalAmount.asc() : indexData.marketTotalAmount.desc();
      default:
        return null;
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


  @Override
  public List<IndexData> findByFiltersWithQueryDSL(
      Long indexInfoId, LocalDate startDate,
      LocalDate endDate, String sortField,
      String sortDirection
  ) {
    QIndexData indexData = QIndexData.indexData;
    JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

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

    PathBuilder<IndexData> path = new PathBuilder<>(IndexData.class, "indexData");
    Order order = "desc".equalsIgnoreCase(sortDirection) ? Order.DESC : Order.ASC;

    OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
        order, path.get(sortField, Comparable.class));

    return queryFactory.selectFrom(indexData).where(builder).orderBy(orderSpecifier).fetch();
  }

  ;

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
