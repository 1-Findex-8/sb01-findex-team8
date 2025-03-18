package com.example.findex.repository;

import com.example.findex.entity.IndexData;
import com.example.findex.entity.QIndexData;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@RequiredArgsConstructor
public class IndexDataRepositoryImpl implements IndexDataRepositoryCustom {

  private final JPAQueryFactory queryFactory;

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
      builder.and(indexData.baseDate.goe(endDate));
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
    query.offset(pageable.getOffset());
    query.limit(pageable.getPageSize());

    return query.fetch();
  }
}
