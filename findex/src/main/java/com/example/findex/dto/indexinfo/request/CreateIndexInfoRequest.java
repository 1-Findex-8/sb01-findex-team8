package com.example.findex.dto.indexinfo.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateIndexInfoRequest(
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    LocalDate basePointInTime,
    BigDecimal baseIndex,
    Boolean favorite) {
}
