package com.example.findex.dto.indexdata.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record RankedIndexPerformanceDto(
    @Schema(description = "지수 성과 정보")
    IndexPerformanceDto performance,

    @Schema(description = "순위", example = "1")
    int rank
) {

}
