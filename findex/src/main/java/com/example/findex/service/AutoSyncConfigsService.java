package com.example.findex.service;

import com.example.findex.dto.autosyncconfigs.response.AutoSyncConfigsDto;
import com.example.findex.dto.autosyncconfigs.response.CursorPageResponseAutoSyncConfigDto;
import com.example.findex.dto.autosyncconfigs.request.AutoSyncConfigsUpdatedRequest;
import com.example.findex.entity.AutoSyncConfigs;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.SourceType;
import com.example.findex.common.error.exception.autosyncconfigs.AutoSyncConfigNotFoundException;
import com.example.findex.mapper.AutoSyncConfigsMapper;
import com.example.findex.repository.IndexInfoRepository;
import com.example.findex.repository.autosyncconfigs.AutoSyncConfigsRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        .orElseThrow(() -> new AutoSyncConfigNotFoundException("존재하지 않는 id" + id));

    autoSyncConfigs.updateActive(request.enabled());

    return autoSyncConfigsMapper.toAutoSyncConfigsDto(autoSyncConfigs);
  }

  @Transactional(readOnly = true)
  public CursorPageResponseAutoSyncConfigDto findAutoSyncConfigsList(Long indexInfoId,
      Boolean enabled, Long idAfter, String cursor, String sortField, String sortDirection, int size) {
    // 추후 indexInfoId에 대한 검증 추가

    Pageable pageable = getPageable(sortField, sortDirection, size);

    Page<AutoSyncConfigs> page = autoSyncConfigsRepository.findAutoSyncConfigsList(
        indexInfoId, enabled, idAfter, pageable);

    return autoSyncConfigsMapper.toCursorPageResponseAutoSyncConfigDto(page);
  }

  private Pageable getPageable(String sortField, String sortDirection, int size) {
    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), convertSortField(sortField));
    return PageRequest.of(0, size, sort);
  }

  private String convertSortField(String sortField) {
    if ("indexInfo.indexName".equals(sortField)) {
      return "indexInfo.indexName";
    } else if ("enabled".equals(sortField)) {
      return "active";
    }
    return sortField;
  }

//  @PostConstruct
  @Transactional
  public void init() { // 테스트를 위한 임시 데이터 생성
    for (int i = 0; i < 20; i++) {
      IndexInfo indexInfo = indexInfoRepository.save(new IndexInfo("indexClassification" + i, "indexName" + i,
          1, LocalDate.now(), BigDecimal.ONE, SourceType.USER, true));
      AutoSyncConfigs autoSyncConfigs = new AutoSyncConfigs(false, indexInfo);
      autoSyncConfigsRepository.save(autoSyncConfigs);
    }

    for (int i = 20; i < 40; i++) {
      IndexInfo indexInfo1 = indexInfoRepository.save(new IndexInfo("indexClassification" + i, "indexName" + i,
          2, LocalDate.now(), BigDecimal.ONE, SourceType.USER, false));
      AutoSyncConfigs autoSyncConfigs = new AutoSyncConfigs(true, indexInfo1);
      autoSyncConfigsRepository.save(autoSyncConfigs);
    }
  }
}
