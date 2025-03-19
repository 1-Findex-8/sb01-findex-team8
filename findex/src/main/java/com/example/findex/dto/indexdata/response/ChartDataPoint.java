package com.example.findex.dto.indexdata.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "차트 데이터 포인트 DTO")
public record ChartDataPoint(
    @Schema(description = "날짜", example = "2023-01-01")
    LocalDate date,

    @Schema(description = "값", example = "2850.75")
    double value
) {
}
