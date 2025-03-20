package com.example.findex.dto.indexdata.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "지수 데이터 수정 요청")
public record IndexDataUpdateRequest(
    @Schema(description = "시가", example = "2800.25")
    BigDecimal marketPrice,

    @Schema(description = "종가", example = "2850.75")
    BigDecimal closingPrice,

    @Schema(description = "고가", example = "2870.5")
    BigDecimal highPrice,

    @Schema(description = "저가", example = "2795.3")
    BigDecimal lowPrice,

    @Schema(description = "전일 대비 등락", example = "50.5")
    BigDecimal versus,

    @Schema(description = "전일 대비 등락률", example = "1.8")
    BigDecimal fluctuationRate,

    @Schema(description = "거래량", example = "1250000")
    Long tradingQuantity,

    @Schema(description = "거래대금", example = "3500000000")
    Long tradingPrice,

    @Schema(description = "상장 시가 총액", example = "450000000000")
    Long marketTotalAmount
) {

}
