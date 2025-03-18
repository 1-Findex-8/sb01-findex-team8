package com.example.findex.dto.autosyncconfigs;

import java.util.List;

public record CursorPageResponseAutoSyncConfigDto(
    List<AutoSyncConfigsDto> content,
    String nextCursor,
    Long nextIdAfter,
    int size,
    Long totalElements,
    boolean hasNext
) {

}
