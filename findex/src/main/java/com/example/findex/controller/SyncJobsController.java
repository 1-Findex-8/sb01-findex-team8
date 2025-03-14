package com.example.findex.controller;

import com.example.findex.dto.syncjobs.response.GetStockMarketIndexResponse;
import com.example.findex.service.SyncJobsService;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/sync-jobs")
@RequiredArgsConstructor
public class SyncJobsController {

  private final SyncJobsService syncJobsService;

  @GetMapping
  public ResponseEntity<GetStockMarketIndexResponse> getStockMarketIndexResponseResponseEntity()
      throws MalformedURLException, URISyntaxException {
    return ResponseEntity.ok(syncJobsService.getStockMarketIndexResponse());
  }
}
