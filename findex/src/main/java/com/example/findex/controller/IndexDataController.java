package com.example.findex.controller;

import com.example.findex.api.IndexDatApi;
import com.example.findex.dto.data.IndexDataDto;
import com.example.findex.dto.request.IndexDataCreateRequest;
import com.example.findex.dto.response.ErrorResponse;
import com.example.findex.service.IndexDataService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/index-data")
@RequiredArgsConstructor
public class IndexDataController implements IndexDatApi {

  private final IndexDataService indexDataService;

  @PostMapping
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
