package com.example.findex.controller;

import com.example.findex.dto.IndexInfo.CreateIndexInfoRequest;
import com.example.findex.dto.IndexInfo.IndexInfoDto;
import com.example.findex.entity.SourceType;
import com.example.findex.service.IndexInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<IndexInfoDto> createIndexInfo(@RequestBody CreateIndexInfoRequest request,
      @RequestParam SourceType sourceType,
      boolean favorite) {

    return ResponseEntity.status(HttpStatus.CREATED).body(indexInfoService.create(request, sourceType, favorite));
  }

}
