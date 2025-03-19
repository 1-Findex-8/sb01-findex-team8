package com.example.findex.mapper;

import com.example.findex.dto.indexinfo.IndexInfoDto;
import com.example.findex.dto.indexinfo.IndexInfoSummaryDto;
import com.example.findex.entity.IndexInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IndexInfoMapper {

  IndexInfoDto toDto(IndexInfo indexInfo);

  @Mapping(target = "indexClassification", source = "indexClassification")
  @Mapping(target = "indexName", source = "indexName")
  IndexInfoSummaryDto toSummaryDto(IndexInfo indexInfo);
}
