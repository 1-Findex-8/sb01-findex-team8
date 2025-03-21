package com.example.findex.repository;

import com.example.findex.entity.IndexData;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface IndexDataRepositoryCustom {
  List<IndexData> findIndexData(Long indexInfoId, LocalDate startDate, LocalDate endDate,
      Long idAfter, String cursor,String sortField, String sortDirection, Pageable pageable);

  long countIndexData(Long indexInfoId, LocalDate startDate, LocalDate endDate);

  List<IndexData> findByFilters(
      Long indexInfoId, LocalDate startDate, LocalDate endDate, String sortField,
      String sortDirection
  );

  List<IndexData> findByFiltersWithQueryDSL(
      Long indexInfoId, LocalDate startDate,
      LocalDate endDate, String sortField,
      String sortDirection
  );
}
