package com.example.findex.dto.IndexInfo;

import java.time.LocalDate;
import java.math.BigDecimal;

public record UpdateIndexInfoRequest(
    int employedItemsCount,
    LocalDate basePointInTime,
    BigDecimal baseIndex,
    boolean favorite) {

}
