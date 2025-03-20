package com.example.findex.controller;

import com.example.findex.dto.indexinfo.CreateIndexInfoRequest;
import com.example.findex.dto.indexinfo.CursorPageResponseIndexInfoDto;
import com.example.findex.dto.indexinfo.IndexInfoDto;
import com.example.findex.dto.indexinfo.IndexInfoSummaryDto;
import com.example.findex.dto.indexinfo.SortDirectionType;
import com.example.findex.dto.indexinfo.UpdateIndexInfoRequest;
import com.example.findex.entity.SourceType;
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
public class IndexInfoController {

  private final IndexInfoService indexInfoService;

  @PostMapping
  public ResponseEntity<IndexInfoDto> createIndexInfo(
      @RequestBody CreateIndexInfoRequest request,
      @RequestParam SourceType sourceType,
      boolean favorite) {

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(indexInfoService.create(request, sourceType, favorite));
  }

  @PatchMapping("{id}")
  public ResponseEntity<IndexInfoDto> updateIndexInfoById(
      @PathVariable Long id,
      @RequestParam UpdateIndexInfoRequest request) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(indexInfoService.update(id, request));
  }

  @GetMapping("{id}")
  public ResponseEntity<IndexInfoDto> findIndexInfoById(@PathVariable Long id) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(indexInfoService.findById(id));
  }

  @GetMapping
  public ResponseEntity<CursorPageResponseIndexInfoDto> findIndexInfoList(
      @RequestParam(value = "indexClassification", required = false) String indexClassification,
      @RequestParam(value = "indexName", required = false) String indexName,
      @RequestParam(value = "favorite", required = false) boolean favorite,
      @RequestParam(value = "idAfter", required = false) Long idAfter,
      @RequestParam(value = "cursor", required = false) String cursor,
      @RequestParam(value = "sortField", required = false) String sortField,
      @RequestParam(value = "sortDirection", required = false) SortDirectionType sortDirection,
      @RequestParam(value = "size", required = false) Integer size
      ) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(indexInfoService.findList(
            indexClassification, indexName, favorite, idAfter, cursor, sortField, sortDirection, size
        ));
  }

  @GetMapping("/summaries")
  public ResponseEntity<List<IndexInfoSummaryDto>> findIndexInfoSummaryList() {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(indexInfoService.findSummaryList());
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteIndexInfo(@PathVariable Long id) {
    indexInfoService.delete(id);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }
}
