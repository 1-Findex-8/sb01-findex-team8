package com.example.findex.dto.indexinfo;

public record FindIndexInfoRequest(
    String indexClassification,
    String indexName,
    boolean favorite,
    Long idAfter,
    String cursor,
    SortFieldType sortField,
    SortDirectionType sortDirection,
    int size
) {

}

