package com.example.findex.controller;

import com.example.findex.api.IndexInfoApi;
import com.example.findex.dto.indexinfo.request.CreateIndexInfoRequest;
import com.example.findex.dto.indexinfo.response.CursorPageResponseIndexInfoDto;
import com.example.findex.dto.indexinfo.IndexInfoDto;
import com.example.findex.dto.indexinfo.IndexInfoSummaryDto;
import com.example.findex.dto.indexinfo.SortDirectionType;
import com.example.findex.dto.indexinfo.request.UpdateIndexInfoRequest;
import com.example.findex.service.IndexInfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/index-infos")
@RequiredArgsConstructor
public class IndexInfoController implements IndexInfoApi {

  private final IndexInfoService indexInfoService;

  @Override
  @PostMapping
  public ResponseEntity<IndexInfoDto> createIndexInfo(
      @RequestBody CreateIndexInfoRequest request) {

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(indexInfoService.create(request));
  }

  @Override
  @PatchMapping("{id}")
  public ResponseEntity<IndexInfoDto> updateIndexInfo(
      @PathVariable Long id,
      @RequestBody UpdateIndexInfoRequest request) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(indexInfoService.update(id, request));
  }

  @Override
  @GetMapping("{id}")
  public ResponseEntity<IndexInfoDto> findIndexInfo(@PathVariable Long id) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(indexInfoService.findById(id));
  }

  @Override
  @GetMapping
  public ResponseEntity<CursorPageResponseIndexInfoDto> findIndexInfoList(
      @RequestParam(value = "indexClassification", required = false) String indexClassification,
      @RequestParam(value = "indexName", required = false) String indexName,
      @RequestParam(value = "favorite", required = false) Boolean favorite,
      @RequestParam(value = "idAfter", required = false) Long idAfter,
      @RequestParam(value = "cursor", required = false) String cursor,
      @RequestParam(value = "sortField", required = false, defaultValue = "indexClassification") String sortField,
      @RequestParam(value = "sortDirection", required = false, defaultValue = "desc") SortDirectionType sortDirection,
      @RequestParam(value = "size", required = false,defaultValue = "10") Integer size
      ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(indexInfoService.findList(
            indexClassification, indexName, favorite, idAfter, cursor, sortField, sortDirection, size
        ));
  }

  @Override
  @GetMapping("/summaries")
  public ResponseEntity<List<IndexInfoSummaryDto>> findIndexInfoSummaryList() {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(indexInfoService.findSummaryList());
  }

  @Override
  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteIndexInfo(@PathVariable Long id) {
    indexInfoService.delete(id);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }
}
