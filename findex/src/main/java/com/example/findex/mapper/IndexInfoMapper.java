package com.example.findex.mapper;

import com.example.findex.dto.indexinfo.IndexInfoDto;
import com.example.findex.dto.indexinfo.IndexInfoSummaryDto;
import com.example.findex.entity.IndexInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IndexInfoMapper {

  IndexInfoDto toDto(IndexInfo indexInfo);

  @Mapping(target = "employeeItemsCount", ignore = true)
  @Mapping(target = "employeeItemsCount", ignore = true)
  @Mapping(target = "basePointInTime", ignore = true)
  @Mapping(target = "baseIndex", ignore = true)
  @Mapping(target = "sourceType", ignore = true)
  @Mapping(target = "favorite", ignore = true)
  IndexInfoSummaryDto toSummaryDto(IndexInfo indexInfo);
}
