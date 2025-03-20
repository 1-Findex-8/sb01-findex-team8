package com.example.findex.dto.indexinfo;

public record FindIndexInfoRequest(
    String indexClassification,
    String indexName,
    Boolean favorite,
    Long idAfter,
    String cursor,
    String sortField,
    SortDirectionType sortDirection,
    int size
) {

}

