package com.example.findex.controller;

import com.example.findex.dto.autosyncconfigs.AutoSyncConfigsDto;
import com.example.findex.dto.autosyncconfigs.request.AutoSyncConfigsUpdatedRequest;
import com.example.findex.service.AutoSyncConfigsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auto-sync-configs")
@RequiredArgsConstructor
public class AutoSyncConfigController {

  private final AutoSyncConfigsService autoSyncConfigsService;

  @PatchMapping("/{id}")
  public ResponseEntity<AutoSyncConfigsDto> updateAutoSyncConfigs(
      @PathVariable Long id, @RequestBody AutoSyncConfigsUpdatedRequest request) {
    return ResponseEntity.ok()
        .body(autoSyncConfigsService.updateAutoSyncConfigs(id, request));
  }

  @GetMapping("")
  public ResponseEntity<?> findAutoSyncConfigsList(
      @RequestParam(value = "indexInfoId", required = false) Long indexInfoId,
      @RequestParam(value = "enabled", required = false) Boolean enabled,
      @RequestParam(value = "idAfter", required = false) Long idAfter,
      @RequestParam(value = "cursor", required = false) Long cursor,
      @RequestParam(value = "sortField", defaultValue = "indexInfo.indexName", required = false) String sortField,
      @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection,
      @RequestParam(value = "size", defaultValue = "10", required = false) int size
  ) {
    autoSyncConfigsService.findAutoSyncConfigsList(
        indexInfoId, enabled, idAfter, cursor, sortField, sortDirection, size);
    return null;
  }
}
