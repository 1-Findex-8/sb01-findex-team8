package com.example.findex.mapper;

import com.example.findex.dto.autosyncconfigs.AutoSyncConfigsDto;
import com.example.findex.entity.AutoSyncConfigs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutoSyncConfigsMapper {

  @Mapping(source = "indexInfo.id", target = "indexInfoId")
  @Mapping(source = "indexInfo.indexClassification", target = "indexClassification")
  @Mapping(source = "indexInfo.indexName", target = "indexName")
  @Mapping(source = "active", target = "enabled")
  AutoSyncConfigsDto toAutoSyncConfigsDto(AutoSyncConfigs autoSyncConfigs);
}
