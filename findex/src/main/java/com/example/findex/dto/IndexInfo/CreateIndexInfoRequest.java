package com.example.findex.dto.IndexInfo;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateIndexInfoRequest(String indexClassification,
                                     String indexName,
                                     int employedItemsCount,
                                     LocalDate basePointInTime,
                                     BigDecimal baseIndex,
                                     boolean favorite) {

}
