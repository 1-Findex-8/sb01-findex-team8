package com.example.findex.controller;

import com.example.findex.api.IndexDataApi;
import com.example.findex.dto.indexdata.data.IndexDataDto;
import com.example.findex.dto.indexdata.request.IndexDataCreateRequest;
import com.example.findex.dto.indexdata.response.ErrorResponse;
import com.example.findex.dto.indexdata.response.IndexPerformanceDto;
import com.example.findex.dto.indexdata.response.RankedIndexPerformanceDto;
import com.example.findex.service.IndexDataService;
import java.util.NoSuchElementException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/index-data")
@RequiredArgsConstructor
public class IndexDataController implements IndexDataApi {

  private final IndexDataService indexDataService;

  @GetMapping("/performance/favorite")
  public ResponseEntity<List<IndexPerformanceDto>> getIndexFavoritePerformanceRank(
      @RequestParam String periodType
  ){
    List<IndexPerformanceDto> dto = indexDataService.getInterestIndexPerformance(periodType);
    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }

  @GetMapping("/performance/rank")
  public ResponseEntity<List<RankedIndexPerformanceDto>> getIndexPerformanceRank(
      @RequestParam String periodType,
      @RequestParam int indexInfoId,
      @RequestParam int limit) {
    List<RankedIndexPerformanceDto> dto = indexDataService.getIndexPerformanceRank(periodType, indexInfoId, limit);
    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }

  @PostMapping
  @Override
  public ResponseEntity<?> createIndexData(@RequestBody IndexDataCreateRequest indexDataCreateRequest) {
    try{
      IndexDataDto indexDto = indexDataService.create(indexDataCreateRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(indexDto);
    }catch (DataIntegrityViolationException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "중복된 지수 및 날짜 조합입니다.", e.getMessage()));
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "해당 ID의 지수 정보를 찾을 수 없습니다.", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다.", e.getMessage()));
    }
  }

//  @GetMapping
//  public ResponseEntity<List<CursorPageResponseIndexDataDto>> getIndexDataList(
//      @RequestParam("indexInfold") long indexInfold,
//      @RequestParam("startDate")LocalDate startDate,
//      @RequestParam("endDate") LocalDate endDate,
//      @RequestParam("idAfter") long idAfter,
//      @RequestParam("sortField") String sortField,
//      @RequestParam("sortDirection") String sortDirection,
//      @RequestParam("size") int size
//  ){
//    List<CursorPageResponseIndexDataDto> response = indexDataService.findAllByIndexInfold(indexInfold);
//    return ResponseEntity.ok(response);
//  }
}
