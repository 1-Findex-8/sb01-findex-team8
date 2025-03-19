package com.example.findex.controller;

import com.example.findex.api.IndexDataApi;
import com.example.findex.dto.indexdata.data.IndexDataDto;
import com.example.findex.dto.indexdata.request.IndexDataCreateRequest;
import com.example.findex.dto.indexdata.response.CursorPageResponseIndexDataDto;
import com.example.findex.dto.indexdata.response.IndexChartDto;
import com.example.findex.dto.indexdata.response.IndexPerformanceDto;
import com.example.findex.dto.indexdata.response.RankedIndexPerformanceDto;
import com.example.findex.entity.IndexData;
import com.example.findex.service.IndexDataService;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/index-data")
@RequiredArgsConstructor
public class IndexDataController implements IndexDataApi {

  private final IndexDataService indexDataService;

  @Override
  public ResponseEntity<List<IndexPerformanceDto>> getIndexFavoritePerformanceRank(
      @RequestParam String periodType
  ){
    List<IndexPerformanceDto> dto = indexDataService.getInterestIndexPerformance(periodType);
    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }

  @Override
  @GetMapping("/performance/rank")
  public ResponseEntity<List<RankedIndexPerformanceDto>> getIndexPerformanceRank(
      @RequestParam String periodType,
      @RequestParam int indexInfoId,
      @RequestParam int limit) {
    List<RankedIndexPerformanceDto> dto = indexDataService.getIndexPerformanceRank(periodType, indexInfoId, limit);
    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }

  @GetMapping("/{indexInfoId}/chart")
  public ResponseEntity<IndexChartDto> getIndexChart(
      @PathVariable int indexInfoId,
      @RequestParam String periodType
  ) {
    IndexChartDto dto = indexDataService.getIndexChart(periodType, indexInfoId);
    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }

  @PostMapping
  @Override
  public ResponseEntity<?> createIndexData(@RequestBody IndexDataCreateRequest indexDataCreateRequest) {
    IndexDataDto indexDto = indexDataService.create(indexDataCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(indexDto);
  }

  @GetMapping
  @Override
  public ResponseEntity<CursorPageResponseIndexDataDto> getIndexDataList(
      @RequestParam(value = "indexInfoId",required = false) Long indexInfoId,
      @RequestParam(value = "startDate",required = false) LocalDate startDate,
      @RequestParam(value = "endDate",required = false) LocalDate endDate,
      @RequestParam(value = "idAfter",required = false) Long idAfter,
      @RequestParam(value = "cursor",required = false) String cursor,
      @RequestParam(value = "sortField",required = false, defaultValue = "baseDate") String sortField,
      @RequestParam(value = "sortDirection",required = false, defaultValue = "desc") String sortDirection,
      @RequestParam(value = "size",required = false, defaultValue = "10") int size
  ){
    CursorPageResponseIndexDataDto response = indexDataService.findIndexDataList(
        indexInfoId,startDate,endDate,idAfter,cursor,sortField,sortDirection,size
    );
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Override
  public ResponseEntity<InputStreamResource> getIndexDataCsv(
      @RequestParam(value = "indexInfoId",required = false) Long indexInfoId,
      @RequestParam(value = "startDate",required = false) LocalDate startDate,
      @RequestParam(value = "endDate",required = false) LocalDate endDate,
      @RequestParam(value = "sortField",required = false, defaultValue = "baseDate") String sortField,
      @RequestParam(value = "sortDirection",required = false, defaultValue = "desc") String sortDirection
  ) {
    List<IndexData> indexDataList = indexDataService.findByFilters(indexInfoId, startDate, endDate, sortField, sortDirection);

    String csvData = convertToCsv(indexDataList);

    ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
    InputStreamResource resource = new InputStreamResource(inputStream);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=index_data.csv");
    headers.add(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.parseMediaType("text/csv"))
        .body(resource);
  }

  private String convertToCsv(List<IndexData> indexDataList) {
    String header = "IndexInfoId, BaseDate, ClosingPrice, HighPrice, LowPrice, Variation, FluctuationRate, TradingQuantity, TradingPrice, MarketCapitalization";

    String body = indexDataList.stream()
        .map(data -> String.join(",",
            String.valueOf(data.getIndexInfo().getId()),
            data.getBaseDate().toString(),
            data.getClosingPrice().toPlainString(),
            data.getHighPrice().toPlainString(),
            data.getLowPrice().toPlainString(),
            data.getVersus().toPlainString(),
            data.getFluctuationRate().toPlainString(),
            String.valueOf(data.getTradingQuantity()),
            String.valueOf(data.getTradingPrice())
        ))
        .collect(Collectors.joining("\n"));

    return header + "\n" + body;
  }
}
