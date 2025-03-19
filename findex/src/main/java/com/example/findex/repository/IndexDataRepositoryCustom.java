package com.example.findex.repository;

import com.example.findex.entity.IndexData;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface IndexDataRepositoryCustom {
  List<IndexData> findIndexData(Long indexInfoId, LocalDate startDate, LocalDate endDate,Long idAfter,
      Pageable pageable);

}
