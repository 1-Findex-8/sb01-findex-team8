package com.example.findex.dto.indexinfo;

import java.time.LocalDate;
import java.math.BigDecimal;

public record UpdateIndexInfoRequest(
    int employedItemsCount,
    LocalDate basePointInTime,
    BigDecimal baseIndex,
    boolean favorite) {

}