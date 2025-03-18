package com.example.findex.repository;

import com.example.findex.entity.IndexData;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexDataRepository extends JpaRepository<IndexData,Long> , IndexDataRepositoryCustom {
  List<IndexData> findByIndexInfoIdInAndBaseDateIn(List<Long> indexInfoIds, List<LocalDate> beforeDate);

  boolean existsByIndexInfoIdAndBaseDate(Long indexInfoId, LocalDate localDate);

  List<IndexData> findByIndexInfoInAndBaseDateIn(List<IndexInfo> indexInfoList, List<LocalDate> beforeDate);

  List<IndexData> findByIndexInfoAndBaseDateBetweenOrderByBaseDateAsc(IndexInfo indexInfo, LocalDate beforeDate, LocalDate today);
}
