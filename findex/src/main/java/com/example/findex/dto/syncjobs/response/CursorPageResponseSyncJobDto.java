package com.example.findex.dto.syncjobs.response;

import java.util.List;

public record CursorPageResponseSyncJobDto(
    List<SyncJobsDto> content,
    String nextCursor,
    Long nextIdAfter,
    int size,
    Long totalElements,
    boolean hasNext
) {

}
