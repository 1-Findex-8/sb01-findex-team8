package com.example.findex.repository.autosyncconfigs;

import com.example.findex.entity.AutoSyncConfigs;
import com.example.findex.entity.QAutoSyncConfigs;
import com.example.findex.entity.QIndexInfo;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class AutoSyncConfigsCustomRepositoryImpl implements AutoSyncConfigsCustomRepository{

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<AutoSyncConfigs> findAutoSyncConfigsList(Long indexInfoId, Boolean enabled,
      Long idAfter, Pageable pageable) {
    QAutoSyncConfigs autoSyncConfigs = QAutoSyncConfigs.autoSyncConfigs;
    QIndexInfo indexInfo = QIndexInfo.indexInfo;

    // 기본 쿼리
    JPAQuery<AutoSyncConfigs> query = jpaQueryFactory
        .selectFrom(autoSyncConfigs);

    if (indexInfoId != null) {
      query.where(autoSyncConfigs.indexInfo.id.eq(indexInfoId));
    }
    else if (idAfter != null) {
      query.where(autoSyncConfigs.id.gt(idAfter));
    }

    if (enabled != null) {
      query.where(autoSyncConfigs.active.eq(enabled));
    }

    OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageable, autoSyncConfigs, indexInfo);
    query.orderBy(orderSpecifier);

    // 데이터 조회
    List<AutoSyncConfigs> content = query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // 전체 레코드 수 조회
    JPAQuery<Long> countQuery = jpaQueryFactory
        .select(autoSyncConfigs.count())
        .from(autoSyncConfigs);

    // indexInfoId 조건을 포함
    if (indexInfoId != null) {
      countQuery.where(autoSyncConfigs.indexInfo.id.eq(indexInfoId));
    }
    // idAfter 조건을 포함
    else if (idAfter != null) {
      countQuery.where(autoSyncConfigs.id.gt(idAfter));
    }

    // enabled 조건을 포함
    if (enabled != null) {
      countQuery.where(autoSyncConfigs.active.eq(enabled));
    }

    long total = Optional.ofNullable(countQuery.fetchOne()).orElse(0L);

    return new PageImpl<>(content, pageable, total);
  }


  private OrderSpecifier<?> getOrderSpecifier(Pageable pageable, QAutoSyncConfigs autoSyncConfigs, QIndexInfo indexInfo) {
    if (!pageable.getSort().isEmpty()) {
      Sort.Order order = pageable.getSort().iterator().next(); // 첫 번째 정렬 기준만 적용
      boolean isAsc = order.isAscending();
      String property = order.getProperty();

      return switch (property) {
        case "indexInfo.indexName" -> isAsc ? indexInfo.indexName.asc() : indexInfo.indexName.desc();
        case "active" -> isAsc ? autoSyncConfigs.active.asc() : autoSyncConfigs.active.desc();
        default -> autoSyncConfigs.id.desc(); // 기본 정렬
      };
    }
    return autoSyncConfigs.id.desc();
  }
}
