package com.example.findex.dto.indexdata.response;

import java.util.List;

public record IndexChartDto(
    int indexInfoId,
    String indexClassification,
    String indexName,
    String periodType,
    List<ChartDataPoint> dataPoints,
    List<ChartDataPoint> ma5DataPoints,
    List<ChartDataPoint> ma20DataPoints
) {

}
