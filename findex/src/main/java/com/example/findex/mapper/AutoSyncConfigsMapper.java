package com.example.findex.mapper;

import com.example.findex.dto.autosyncconfigs.response.AutoSyncConfigsDto;
import com.example.findex.dto.autosyncconfigs.response.CursorPageResponseAutoSyncConfigDto;
import com.example.findex.entity.AutoSyncConfigs;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface AutoSyncConfigsMapper {

  @Mapping(source = "indexInfo.id", target = "indexInfoId")
  @Mapping(source = "indexInfo.indexClassification", target = "indexClassification")
  @Mapping(source = "indexInfo.indexName", target = "indexName")
  @Mapping(source = "active", target = "enabled")
  AutoSyncConfigsDto toAutoSyncConfigsDto(AutoSyncConfigs autoSyncConfigs);

  List<AutoSyncConfigsDto> toAutoSyncConfigsDtoList(List<AutoSyncConfigs> autoSyncConfigsList);

  default CursorPageResponseAutoSyncConfigDto toCursorPageResponseAutoSyncConfigDto(
      Page<AutoSyncConfigs> page) {
    List<AutoSyncConfigsDto> content = toAutoSyncConfigsDtoList(page.getContent());

    Long nextIdAfter = !content.isEmpty() ? content.get(content.size() - 1).id() : null;
    String nextCursor = nextIdAfter != null ? nextIdAfter.toString() : null;
    
    return new CursorPageResponseAutoSyncConfigDto(
        content,
        nextCursor,
        nextIdAfter,
        page.getSize(),
        page.getTotalElements(),
        page.hasNext()
    );
  }
}

