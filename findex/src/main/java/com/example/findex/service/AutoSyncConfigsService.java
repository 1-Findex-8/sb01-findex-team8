package com.example.findex.service;

import com.example.findex.dto.autosyncconfigs.AutoSyncConfigsDto;
import com.example.findex.dto.autosyncconfigs.request.AutoSyncConfigsUpdatedRequest;
import com.example.findex.entity.AutoSyncConfigs;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.SourceType;
import com.example.findex.global.error.exception.autosyncconfigs.AutoSyncConfigNotFoundException;
import com.example.findex.mapper.AutoSyncConfigsMapper;
import com.example.findex.repository.AutoSyncConfigsRepository;
import com.example.findex.repository.IndexInfoRepository;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AutoSyncConfigsService {

  private final AutoSyncConfigsRepository autoSyncConfigsRepository;
  private final AutoSyncConfigsMapper autoSyncConfigsMapper;

  private final IndexInfoRepository indexInfoRepository;

  @Transactional
  public AutoSyncConfigsDto updateAutoSyncConfigs(Long id, AutoSyncConfigsUpdatedRequest request) {
    AutoSyncConfigs autoSyncConfigs = autoSyncConfigsRepository.findById(id)
        .orElseThrow(() -> new AutoSyncConfigNotFoundException());

    autoSyncConfigs.updateActive(request.enabled());

    AutoSyncConfigsDto autoSyncConfigsDto = autoSyncConfigsMapper.toAutoSyncConfigsDto(autoSyncConfigs);
    return autoSyncConfigsDto;
  }

  @PostConstruct
  void init() { // 테스트를 위한 임시 데이터 생성
    IndexInfo indexInfo = new IndexInfo("indexClassification", "indexName",
        1, LocalDate.now(), BigDecimal.ONE, SourceType.USER, false);
    AutoSyncConfigs autoSyncConfigs = new AutoSyncConfigs(false, indexInfoRepository.save(indexInfo));
    autoSyncConfigsRepository.save(autoSyncConfigs);
  }
}
