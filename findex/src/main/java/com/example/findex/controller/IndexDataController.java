package com.example.findex.controller;

import com.example.findex.api.IndexDataApi;
import com.example.findex.dto.indexdata.data.IndexDataDto;
import com.example.findex.dto.indexdata.request.IndexDataCreateRequest;
import com.example.findex.dto.indexdata.request.IndexDataUpdateRequest;
import com.example.findex.dto.indexdata.response.CursorPageResponseIndexDataDto;
import com.example.findex.dto.indexdata.response.IndexChartDto;
import com.example.findex.dto.indexdata.response.IndexPerformanceDto;
import com.example.findex.dto.indexdata.response.RankedIndexPerformanceDto;
import com.example.findex.service.IndexDataService;
import com.querydsl.core.types.Order;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
  @GetMapping("/performance/favorite")
  public ResponseEntity<List<IndexPerformanceDto>> getIndexFavoritePerformanceRank(
      @RequestParam(required = false, defaultValue = "DAILY") String periodType
  ){
    List<IndexPerformanceDto> dto = indexDataService.getInterestIndexPerformance(periodType);
    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }

  @Override
  @GetMapping("/performance/rank")
  public ResponseEntity<List<RankedIndexPerformanceDto>> getIndexPerformanceRank(
      @RequestParam(required = false, defaultValue = "DAILY") String periodType,
      @RequestParam(required = false) Integer indexInfoId,
      @RequestParam(required = false, defaultValue = "10") Integer limit) {
    List<RankedIndexPerformanceDto> dto = indexDataService.getIndexPerformanceRank(periodType, indexInfoId, limit);
    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }

  @Override
  @GetMapping("/{indexInfoId}/chart")
  public ResponseEntity<IndexChartDto> getIndexChart(
      @PathVariable int indexInfoId,
      @RequestParam(required = false, defaultValue = "DAILY")  String periodType
  ) {
    IndexChartDto dto = indexDataService.getIndexChart(periodType, indexInfoId);
    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }

  @PostMapping("")
  @Override
  public ResponseEntity<?> createIndexData(@RequestBody IndexDataCreateRequest indexDataCreateRequest) {
    IndexDataDto response = indexDataService.create(indexDataCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("")
  @Override
  public ResponseEntity<CursorPageResponseIndexDataDto> getIndexDataList(
      @RequestParam(value = "indexInfoId",required = false) Long indexInfoId,
      @RequestParam(value = "startDate",required = false) LocalDate startDate,
      @RequestParam(value = "endDate",required = false) LocalDate endDate,
      @RequestParam(value = "idAfter",required = false) Long idAfter,
      @RequestParam(value = "cursor",required = false) String cursor,
      @RequestParam(value = "sortField",required = false, defaultValue = "baseDate") String sortField,
      @RequestParam(value = "sortDirection",required = false, defaultValue = "desc") String sortDirection,
      @RequestParam(value = "size",required = false, defaultValue = "10") Integer size
  ){
    CursorPageResponseIndexDataDto response = indexDataService.findIndexDataList(
        indexInfoId, startDate, endDate, idAfter,
        sortField, String.valueOf("asc".equalsIgnoreCase(sortDirection) ? Order.ASC : Order.DESC), size);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PatchMapping("/{id}")
  @Override
  public ResponseEntity<IndexDataDto> updateIndexData(
      @PathVariable Long id, @RequestBody IndexDataUpdateRequest indexDataUpdateRequest) {
    IndexDataDto response = indexDataService.updateIndexData(id, indexDataUpdateRequest);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{id}")
  @Override
  public ResponseEntity<Void> deleteIndexData(@PathVariable Long id) {
    indexDataService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Override
  @GetMapping("/export/csv")
  public ResponseEntity<InputStreamResource> getIndexDataCsv(
      @RequestParam(value = "indexInfoId",required = false) Long indexInfoId,
      @RequestParam(value = "startDate",required = false) LocalDate startDate,
      @RequestParam(value = "endDate",required = false) LocalDate endDate,
      @RequestParam(value = "sortField",required = false, defaultValue = "baseDate") String sortField,
      @RequestParam(value = "sortDirection",required = false, defaultValue = "desc") String sortDirection
  ) {

    String csvData = indexDataService.findToCsv(indexInfoId, startDate, endDate, sortField, sortDirection);

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
}
