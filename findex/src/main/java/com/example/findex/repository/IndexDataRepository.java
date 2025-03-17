package com.example.findex.repository;

import com.example.findex.dto.response.IndexPerformanceDto;
import com.example.findex.entity.IndexData;
import com.example.findex.entity.IndexInfo;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexDataRepository extends JpaRepository<IndexData,Long> {
  List<IndexData> findByIndexInfoIdInAndBaseDateIn(List<Long> indexInfoIds, List<LocalDate> beforeDate);
}
