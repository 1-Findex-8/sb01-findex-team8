package com.example.findex.mapper;

import com.example.findex.dto.syncjobs.response.SyncJobsDto;
import com.example.findex.entity.SyncJobs;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SyncJobsMapper {

  @Mapping(source = "indexInfo.id", target = "indexInfoId")
  SyncJobsDto toSyncJobsDto(SyncJobs syncJobs);

  List<SyncJobsDto> toSyncJobsDtoList(List<SyncJobs> syncJobsList);
}
