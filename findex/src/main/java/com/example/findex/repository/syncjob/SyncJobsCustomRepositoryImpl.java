package com.example.findex.repository.syncjob;

import static com.example.findex.entity.QSyncJobs.syncJobs;

import com.example.findex.entity.JobType;
import com.example.findex.entity.Result;
import com.example.findex.entity.SyncJobs;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class SyncJobsCustomRepositoryImpl implements SyncJobsCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<SyncJobs> findSyncJobsList(JobType jobType, Long indexInfoId, LocalDate baseDateFrom,
      LocalDate baseDateTo, String worker, LocalDateTime jobTimeFrom, LocalDateTime jobTimeTo,
      Result status, Long idAfter, String cursor, Pageable pageable) {

    JPAQuery<SyncJobs> query = jpaQueryFactory.selectFrom(syncJobs);

    // 조건 추가
    query.where(
        eqJobType(jobType),
        eqIndexInfoId(indexInfoId),
        goeBaseDate(baseDateFrom),
        loeBaseDate(baseDateTo),
        eqWorker(worker),
        goeJobTime(jobTimeFrom),
        loeJobTime(jobTimeTo),
        eqStatus(status),
        gtIdAfter(idAfter)
    );

    OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageable);
    query.orderBy(orderSpecifier);

    // 전체 레코드 조회
    List<SyncJobs> content = query
        .limit(pageable.getPageSize())
        .fetch();

    // 전체 레코드 수 조회
    JPAQuery<Long> countQuery = jpaQueryFactory
        .select(syncJobs.count())
        .from(syncJobs);

    countQuery.where(
        eqJobType(jobType),
        eqIndexInfoId(indexInfoId),
        goeBaseDate(baseDateFrom),
        loeBaseDate(baseDateTo),
        eqWorker(worker),
        goeJobTime(jobTimeFrom),
        loeJobTime(jobTimeTo),
        eqStatus(status),
        gtIdAfter(idAfter)
    );

    long total = Optional.ofNullable(countQuery.fetchOne()).orElse(0L);

    return new PageImpl<>(content, pageable, total);
  }

  private OrderSpecifier<?> getOrderSpecifier(Pageable pageable) {
    if (!pageable.getSort().isEmpty()) {
      Sort.Order order = pageable.getSort().iterator().next(); // 첫 번째 정렬 기준만 적용
      boolean isAsc = order.isAscending();
      String property = order.getProperty();

      return switch (property) {
        case "targetDate" -> isAsc ? syncJobs.targetDate.asc() : syncJobs.targetDate.desc();
        case "jobTime" -> isAsc ? syncJobs.jobTime.asc() : syncJobs.jobTime.desc();
        default -> syncJobs.id.desc(); // 기본 정렬
      };
    }
    return syncJobs.id.desc();
  }

  private BooleanExpression eqJobType(JobType jobType) {
    return jobType != null ? syncJobs.jobType.eq(jobType) : null;
  }

  private BooleanExpression eqIndexInfoId(Long indexInfoId) {
    return indexInfoId != null ? syncJobs.indexInfo.id.eq(indexInfoId) : null;
  }

  private BooleanExpression goeBaseDate(LocalDate baseDateFrom) {
    return baseDateFrom != null ? syncJobs.targetDate.goe(baseDateFrom) : null;
  }

  private BooleanExpression loeBaseDate(LocalDate baseDateTo) {
    return baseDateTo != null ? syncJobs.targetDate.loe(baseDateTo) : null;
  }

  private BooleanExpression eqWorker(String worker) {
    return worker != null ? syncJobs.worker.eq(worker) : null;
  }

  private BooleanExpression goeJobTime(LocalDateTime jobTimeFrom) {
    return jobTimeFrom != null ? syncJobs.jobTime.goe(jobTimeFrom) : null;
  }

  private BooleanExpression loeJobTime(LocalDateTime jobTimeTo) {
    return jobTimeTo != null ? syncJobs.jobTime.loe(jobTimeTo) : null;
  }

  private BooleanExpression eqStatus(Result status) {
    return status != null ? syncJobs.result.eq(status) : null;
  }

  private BooleanExpression gtIdAfter(Long idAfter) {
    return idAfter != null ? syncJobs.id.gt(idAfter) : null;
  }
}
