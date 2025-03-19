package com.example.findex.mapper;

import com.example.findex.dto.syncjobs.response.CursorPageResponseSyncJobDto;
import com.example.findex.dto.syncjobs.response.SyncJobsDto;
import com.example.findex.entity.SyncJobs;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface SyncJobsMapper {

  @Mapping(source = "indexInfo.id", target = "indexInfoId")
  SyncJobsDto toSyncJobsDto(SyncJobs syncJobs);

  List<SyncJobsDto> toSyncJobsDtoList(List<SyncJobs> syncJobsList);

  default CursorPageResponseSyncJobDto toCursorPageResponseSyncJobDto(Page<SyncJobs> page) {
    List<SyncJobsDto> content = toSyncJobsDtoList(page.getContent());

    Long nextIdAfter = !content.isEmpty() ? content.get(content.size() - 1).id() : null;
    String nextCursor = !content.isEmpty() ?
        page.getContent().get(page.getContent().size() - 1).getCreatedAt().toString() : null;

    return new CursorPageResponseSyncJobDto(
        content,
        nextCursor,
        nextIdAfter,
        page.getSize(),
        page.getTotalElements(),
        page.hasNext()
    );
  }
}
