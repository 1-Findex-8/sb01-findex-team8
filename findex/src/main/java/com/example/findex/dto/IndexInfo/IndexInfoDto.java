package com.example.findex.dto.IndexInfo;

import com.example.findex.entity.SourceType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record IndexInfoDto(
    Long id,
    String indexClassification,
    String indexName,
    int employeeItemsCount,
    LocalDate basePointInTime,
    BigDecimal baseIndex,
    SourceType sourceType,
    boolean favorite) {
}
