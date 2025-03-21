package com.example.findex.dto.indexinfo.request;

import com.example.findex.dto.indexinfo.SortDirectionType;

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

