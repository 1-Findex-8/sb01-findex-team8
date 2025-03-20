package com.example.findex.repository;

import com.example.findex.entity.IndexData;
import com.example.findex.entity.IndexInfo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexDataRepository extends JpaRepository<IndexData,Long> , IndexDataRepositoryCustom {
  
  List<IndexData> findByIndexInfoIdInAndBaseDateIn(List<Long> indexInfoIds, List<LocalDate> beforeDate);

  boolean existsByIndexInfoIdAndBaseDate(Long indexInfoId, LocalDate localDate);

  Optional<IndexData> findByIndexInfoAndBaseDate(IndexInfo indexInfo, LocalDate baseDate);

  List<IndexData> findByIndexInfoInAndBaseDateIn(List<IndexInfo> indexInfoList, List<LocalDate> beforeDate);

  List<IndexData> findByIndexInfoAndBaseDateBetweenOrderByBaseDateAsc(IndexInfo indexInfo, LocalDate beforeDate, LocalDate today);

  void deleteByIndexInfo(IndexInfo indexInfo);
}
