package com.example.findex.dto.indexinfo;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateIndexInfoRequest(
    int employedItemsCount,
    LocalDate basePointInTime,
    BigDecimal baseIndex,
    boolean favorite) {

}