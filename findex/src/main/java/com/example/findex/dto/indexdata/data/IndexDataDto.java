package com.example.findex.dto.indexdata.data;

import com.example.findex.entity.SourceType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "지수 데이터 DTO")
public record IndexDataDto(
    @Schema(description = "지수 데이터 ID", example = "1")
    Long id, // 지수 데이터 ID

    @Schema(description = "지수 정보 ID", example = "1")
    Long indexInfoId, // 지수 정보 ID

    @Schema(description = "기준 일자", example = "2023-01-01")
    LocalDate baseDate, // 기준 일자

    @Schema(description = "출처 (사용자, Open API)", example = "OPEN_API", allowableValues = {"USER", "OPEN_API"})
    SourceType sourceType, // 출처

    @Schema(description = "시가", example = "2800.25")
    BigDecimal marketPrice, // 시가

    @Schema(description = "종가", example = "2850.75")
    BigDecimal closingPrice, // 종가

    @Schema(description = "고가", example = "2870.5")
    BigDecimal highPrice, // 고가

    @Schema(description = "저가", example = "2795.3")
    BigDecimal lowPrice, // 저가

    @Schema(description = "전일 대비 등락", example = "50.5")
    BigDecimal versus, // 전일 대비 등락

    @Schema(description = "전일 대비 등락률", example = "1.8")
    BigDecimal fluctuationRate, // 전일 대비 등락률

    @Schema(description = "거래량", example = "1250000")
    Long tradingQuantity, // 거래량

    @Schema(description = "거래대금", example = "3500000000")
    Long tradingPrice, // 거래대금

    @Schema(description = "상장 시가 총액", example = "450000000000")
    Long marketTotalAmount // 상장 시가 총액
) {
}
