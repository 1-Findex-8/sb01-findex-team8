package com.example.findex.controller;

import com.example.findex.api.SyncJobApi;
import com.example.findex.dto.syncjobs.request.IndexDataSyncRequest;
import com.example.findex.dto.syncjobs.response.CursorPageResponseSyncJobDto;
import com.example.findex.dto.syncjobs.response.SyncJobsDto;
import com.example.findex.entity.JobType;
import com.example.findex.entity.Result;
import com.example.findex.service.SyncJobsService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/sync-jobs")
@RequiredArgsConstructor
public class SyncJobsController implements SyncJobApi {

  private final SyncJobsService syncJobsService;

  @PostMapping("/index-infos")
  public ResponseEntity<List<SyncJobsDto>> syncIndexInfos(HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.ACCEPTED)
        .body(syncJobsService.syncIndexInfos(request));
  }

  @PostMapping("/index-data")
  public ResponseEntity<List<SyncJobsDto>> syncIndexData(
      @RequestBody IndexDataSyncRequest request, HttpServletRequest httpServletRequest) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(syncJobsService.syncIndexData(request, httpServletRequest));
  }

  @GetMapping("")
  public ResponseEntity<CursorPageResponseSyncJobDto> findSyncJobsList(
      @RequestParam(value = "jobType", required = false) JobType jobType,
      @RequestParam(value = "indexInfoId", required = false) Long indexInfoId,
      @RequestParam(value = "baseDateFrom", required = false) LocalDate baseDateFrom,
      @RequestParam(value = "baseDateTo", required = false) LocalDate baseDateTo,
      @RequestParam(value = "worker", required = false) String worker,
      @RequestParam(value = "jobTimeFrom", required = false) LocalDateTime jobTimeFrom,
      @RequestParam(value = "jobTimeTo", required = false) LocalDateTime jobTimeTo,
      @RequestParam(value = "status", required = false) Result status,
      @RequestParam(value = "idAfter", required = false) Long idAfter,
      @RequestParam(value = "cursor", required = false) String cursor,
      @RequestParam(value = "sortField", defaultValue = "jobTime", required = false) String sortField,
      @RequestParam(value = "sortDirection", defaultValue = "desc", required = false) String sortDirection,
      @RequestParam(value = "size", defaultValue = "10", required = false) int size
  ) {
    return ResponseEntity.ok()
        .body(syncJobsService.findSyncJobList(jobType, indexInfoId, baseDateFrom, baseDateTo, worker,
        jobTimeFrom, jobTimeTo, status, idAfter, cursor, sortField, sortDirection, size));
  }
}
