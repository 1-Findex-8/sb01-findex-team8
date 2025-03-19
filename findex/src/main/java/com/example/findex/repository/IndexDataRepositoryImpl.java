package com.example.findex.repository;

import com.example.findex.entity.IndexData;
import com.example.findex.entity.QIndexData;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
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
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
public class IndexDataRepositoryImpl implements IndexDataRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<IndexData> findIndexData(Long indexInfoId, LocalDate startDate, LocalDate endDate,
      Long idAfter, Pageable pageable) {
    QIndexData indexData = QIndexData.indexData;
    BooleanBuilder builder = new BooleanBuilder();

    //indexInfoId 일치
    builder.and(indexData.id.eq(indexInfoId));

    //startDate 조건
    if(startDate != null) {
      builder.and(indexData.baseDate.goe(startDate));
    }

    //endDate 조건
    if(endDate != null) {
      builder.and(indexData.baseDate.loe(endDate));
    }

    //idAfter 조건
    if(idAfter != null) {
      builder.and(indexData.id.gt(idAfter));
    }

    JPQLQuery<IndexData> query = queryFactory.selectFrom(indexData)
        .where(builder);

    // Pageable 정렬 조건 적용
    if (pageable.getSort() != null) {
      for (Sort.Order order : pageable.getSort()) {
        PathBuilder<IndexData> path = new PathBuilder<>(IndexData.class, "indexData");
        Order orderDirection = order.isAscending() ? Order.ASC : Order.DESC;
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
            orderDirection,
            path.get(order.getProperty(), Comparable.class)
        );
        query.orderBy(orderSpecifier);
      }
    }

    // 페이징 처리
    query.offset(pageable.getOffset()); // 조회 시작 위치
    query.limit(pageable.getPageSize()); // 조회할 데이터 건수

    return query.fetch();
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
