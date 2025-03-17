package com.example.findex.mapper;

import com.example.findex.dto.IndexInfo.IndexInfoDto;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.SourceType;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IndexInfoMapper {

  public IndexInfo toEntity(String indexClassification, String indexName,
      int employeeItemsCount, LocalDate basePointInTime, BigDecimal baseIndex, SourceType sourceType,
      boolean favorite) {
    return new IndexInfo(indexClassification, indexName, employeeItemsCount, basePointInTime, baseIndex, sourceType,
        favorite);
  }

  public IndexInfoDto toDto(IndexInfo indexInfo) {
    return new IndexInfoDto(indexInfo.getId(), indexInfo.getIndexClassification(),
        indexInfo.getIndexName(), indexInfo.getEmployeeItemsCount(),
        indexInfo.getBasePointInTime(), indexInfo.getBaseIndex(),
        indexInfo.getSourceType(), indexInfo.isFavorite());
  }
}
