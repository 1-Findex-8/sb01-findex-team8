package com.example.findex.controller;

import com.example.findex.dto.syncjobs.response.GetStockMarketIndexResponse;
import com.example.findex.dto.syncjobs.response.SyncJobsDto;
import com.example.findex.service.SyncJobsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/sync-jobs")
@RequiredArgsConstructor
public class SyncJobsController {

  private final SyncJobsService syncJobsService;

  @GetMapping
  public ResponseEntity<GetStockMarketIndexResponse> getStockMarketIndexResponseResponseEntity() {
    return ResponseEntity.ok(syncJobsService.getStockMarketIndexResponse());
  }

  @PostMapping("/index-infos")
  public ResponseEntity<List<SyncJobsDto>> syncIndexInfos() {
    return ResponseEntity.ok()
        .body(syncJobsService.syncIndexInfos());
  }
}
