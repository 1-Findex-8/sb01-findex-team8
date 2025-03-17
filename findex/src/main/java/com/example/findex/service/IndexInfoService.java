package com.example.findex.service;

import com.example.findex.dto.IndexInfo.CreateIndexInfoRequest;
import com.example.findex.dto.IndexInfo.IndexInfoDto;
import com.example.findex.dto.IndexInfo.UpdateIndexInfoRequest;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.SourceType;
import com.example.findex.mapper.IndexInfoMapper;
import com.example.findex.repository.IndexInfoRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexInfoService {

  private final IndexInfoRepository indexInfoRepository;
  private final IndexInfoMapper indexInfoMapper;

  public IndexInfoDto create(CreateIndexInfoRequest request, SourceType sourceType, boolean favorite) {
    if (indexInfoRepository.existsByIndexClassificationAndIndexName(request.indexClassification(), request.indexName())) {
      throw new DataIntegrityViolationException("지수 분류명과 지수명 조합은 중복될 수 없습니다.");
    }
    IndexInfo indexInfo = new IndexInfo(request.indexClassification(),
        request.indexName(),
        request.employedItemsCount(),
        request.basePointInTime(),
        request.baseIndex(),
        sourceType,
        favorite);

    return indexInfoMapper.toDto(indexInfoRepository.save(indexInfo));
  }

  public IndexInfoDto update(Long indexInfoId, UpdateIndexInfoRequest request) {
    IndexInfo indexInfo = indexInfoRepository.findById(indexInfoId).orElseThrow(() -> new NoSuchElementException("id: " + indexInfoId));

    // TODO: update 로직

    indexInfoRepository.save(indexInfo);
    return indexInfoMapper.toDto(indexInfo);
  }

}
