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
      Long idAfter, String cursor, String sortField, String sortDirection, Pageable pageable) {

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

    // 커서 기반 페이지네이션 조건: 정렬방향에 따라
    if (idAfter != null) {
      if ("asc".equalsIgnoreCase(sortDirection)) {
        builder.and(indexData.id.gt(idAfter));
      } else {
        builder.and(indexData.id.lt(idAfter));
      }
    }

    // 동적 정렬 조건 구성
    OrderSpecifier<?> orderSpecifier;
    if ("baseDate".equalsIgnoreCase(sortField)) {
      orderSpecifier = "asc".equalsIgnoreCase(sortDirection)
          ? indexData.baseDate.asc()
          : indexData.baseDate.desc();
    } else if ("marketPrice".equalsIgnoreCase(sortField)) {
      orderSpecifier = "asc".equalsIgnoreCase(sortDirection)
          ? indexData.marketPrice.asc()
          : indexData.marketPrice.desc();
    } else if ("closingPrice".equalsIgnoreCase(sortField)) {
      orderSpecifier = "asc".equalsIgnoreCase(sortDirection)
          ? indexData.closingPrice.asc()
          : indexData.closingPrice.desc();
    } else if ("highPrice".equalsIgnoreCase(sortField)) {
      orderSpecifier = "asc".equalsIgnoreCase(sortDirection)
          ? indexData.highPrice.asc()
          : indexData.highPrice.desc();
    } else if ("lowPrice".equalsIgnoreCase(sortField)) {
      orderSpecifier = "asc".equalsIgnoreCase(sortDirection)
          ? indexData.lowPrice.asc()
          : indexData.lowPrice.desc();
    } else if ("versus".equalsIgnoreCase(sortField)) {
      orderSpecifier = "asc".equalsIgnoreCase(sortDirection)
          ? indexData.versus.asc()
          : indexData.versus.desc();
    } else if ("fluctuationRate".equalsIgnoreCase(sortField)) {
      orderSpecifier = "asc".equalsIgnoreCase(sortDirection)
          ? indexData.fluctuationRate.asc()
          : indexData.fluctuationRate.desc();
    } else if ("tradingQuantity".equalsIgnoreCase(sortField)) {
      orderSpecifier = "asc".equalsIgnoreCase(sortDirection)
          ? indexData.tradingQuantity.asc()
          : indexData.tradingQuantity.desc();
    } else if ("tradingPrice".equalsIgnoreCase(sortField)) {
      orderSpecifier = "asc".equalsIgnoreCase(sortDirection)
          ? indexData.tradingPrice.asc()
          : indexData.tradingPrice.desc();
    } else if ("marketTotalAmount".equalsIgnoreCase(sortField)) {
      orderSpecifier = "asc".equalsIgnoreCase(sortDirection)
          ? indexData.marketTotalAmount.asc()
          : indexData.marketTotalAmount.desc();
    } else {
      // 기본 정렬: id 기준
      orderSpecifier = "asc".equalsIgnoreCase(sortDirection)
          ? indexData.id.asc()
          : indexData.id.desc();
    }

    // QueryDSL Query 생성
    JPAQuery<IndexData> query = queryFactory
        .selectFrom(indexData)
        .where(builder)
        .orderBy(orderSpecifier);

    return query.fetch();
  }

  //전체 데이터 개수 조회
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

    Long count = queryFactory
        .select(indexData.count())
        .from(indexData)
        .where(builder)
        .fetchOne();

    return count != null ? count : 0L;
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
