package com.example.findex.repository;

import com.example.findex.entity.IndexData;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexDataRepository extends JpaRepository<IndexData,Long> {

  boolean existsByIndexInfoIdAndBaseDate(Long indexInfoId, LocalDate localDate);

  @Query("SELECT i FROM IndexData i "
      + "WHERE i.indexInfo.id = :indexInfoId "
      + "AND i.baseDate BETWEEN :startDate AND :endDate "
      + "AND i.id > :idAfter")
  Page<IndexData> findIndexDataList(long indexInfoId, LocalDate startDate, LocalDate endDate, long idAfter, Pageable pageable);
}
