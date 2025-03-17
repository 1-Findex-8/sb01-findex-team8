package com.example.findex.mapper;

import com.example.findex.dto.IndexInfo.IndexInfoDto;
import com.example.findex.entity.IndexInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IndexInfoMapper {

  public IndexInfoDto toDto(IndexInfo indexInfo) {
    return new IndexInfoDto(
        indexInfo.getId(),
        indexInfo.getIndexClassification(),
        indexInfo.getIndexName(),
        indexInfo.getEmployeeItemsCount(),
        indexInfo.getBasePointInTime(),
        indexInfo.getBaseIndex(),
        indexInfo.getSourceType(),
        indexInfo.isFavorite());
  }
}
