package com.example.findex.dto.autosyncconfigs.response;

public record AutoSyncConfigsDto(
    Long id,
    Long indexInfoId,
    String indexClassification,
    String indexName,
    boolean enabled
) {

}
