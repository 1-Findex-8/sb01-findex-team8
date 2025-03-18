package com.example.findex.controller;

import com.example.findex.dto.indexinfo.CreateIndexInfoRequest;
import com.example.findex.dto.indexinfo.FindIndexInfoRequest;
import com.example.findex.dto.indexinfo.IndexInfoDto;
import com.example.findex.dto.indexinfo.UpdateIndexInfoRequest;
import com.example.findex.entity.SourceType;
import com.example.findex.service.IndexInfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping("{id}")
  public ResponseEntity<IndexInfoDto> findIndexInfoById(@PathVariable Long id) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(indexInfoService.findById(id));
  }

  @GetMapping
  public ResponseEntity<List<IndexInfoDto>> findIndexInfoAndSort(
      @RequestBody FindIndexInfoRequest request) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(indexInfoService.findAndSort(request));
  }
}
