package com.example.findex.repository.indexinfo;

import com.example.findex.dto.indexinfo.SortDirectionType;
import com.example.findex.entity.IndexInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexInfoRepositoryCustom {
  Page<IndexInfo> findByFilters(
      String indexClassification,
      String indexName,
      Boolean favorite,
      Long idAfter,
      String cursor,
      String sortField,
      SortDirectionType sortDirectionType,
      int size);

//  Long countByFilters(String indexClassification, String indexName, Boolean favorite);
}
