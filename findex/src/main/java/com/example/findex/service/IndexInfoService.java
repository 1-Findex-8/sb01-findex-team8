package com.example.findex.service;

import com.example.findex.dto.indexinfo.CreateIndexInfoRequest;
import com.example.findex.dto.indexinfo.CursorPageResponseIndexInfoDto;
import com.example.findex.dto.indexinfo.IndexInfoDto;
import com.example.findex.dto.indexinfo.IndexInfoSummaryDto;
import com.example.findex.dto.indexinfo.SortDirectionType;
import com.example.findex.dto.indexinfo.UpdateIndexInfoRequest;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.SourceType;
import com.example.findex.global.error.exception.indexinfo.IndexInfoDuplicateException;
import com.example.findex.global.error.exception.indexinfo.IndexInfoNotFoundException;
import com.example.findex.mapper.IndexInfoMapper;
import com.example.findex.repository.IndexDataRepository;
import com.example.findex.repository.indexinfo.IndexInfoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IndexInfoService {

  private final IndexInfoRepository indexInfoRepository;
  private final IndexInfoMapper indexInfoMapper;
  private final IndexDataRepository indexDataRepository;
//  private final IndexInfoRepositoryCustom indexInfoRepositoryCustom;

  public IndexInfoDto create(CreateIndexInfoRequest request) {

    if (indexInfoRepository.existsByIndexClassificationAndIndexName(request.indexClassification(), request.indexName())) {
      throw new IndexInfoDuplicateException();
    }

    IndexInfo indexInfo = new IndexInfo(
        request.indexClassification(),
        request.indexName(),
        request.employedItemsCount(),
        request.basePointInTime(),
        request.baseIndex(),
        SourceType.USER,
        request.favorite());

    return indexInfoMapper.toDto(indexInfoRepository.save(indexInfo));
  }

  @Transactional
  public IndexInfoDto update(Long indexInfoId, UpdateIndexInfoRequest request) {
    IndexInfo indexInfo = indexInfoRepository.findById(indexInfoId)
        .orElseThrow(IndexInfoNotFoundException::new);

    if (request.employedItemsCount() != null) {
      indexInfo.updateEmployedItemsCount(request.employedItemsCount());
    }
    if (request.basePointInTime() != null) {
      indexInfo.updateBasePointInTime(request.basePointInTime());
    }
    if (request.baseIndex() != null) {
      indexInfo.updateBaseIndex(request.baseIndex());
    }
    if (request.favorite() != null) {
      indexInfo.updateFavorite(request.favorite());
    }

    return indexInfoMapper.toDto(indexInfoRepository.save(indexInfo));
  }

  public IndexInfoDto findById(Long indexInfoId) {
    return indexInfoMapper.toDto(
        indexInfoRepository.findById(indexInfoId)
            .orElseThrow(IndexInfoNotFoundException::new));
  }

  @Transactional
  public CursorPageResponseIndexInfoDto findList(
      String indexClassification,
      String indexName,
      Boolean favorite,
      Long idAfter,
      String cursor,
      String sortField,
      SortDirectionType sortDirectionType,
      int size) {

    Pageable pageable = PageRequest.of(0, size);

    Page<IndexInfo> page = indexInfoRepository.findByFilters(
        indexClassification,
        indexName,
        favorite,
        idAfter,
        cursor,
        sortField,
        sortDirectionType,
        pageable
    );

    List<IndexInfoDto> content = page.getContent()
        .stream()
        .map(indexInfoMapper::toDto)
        .toList();

    Long nextIdAfter = content.isEmpty() ? null : content.get(content.size() - 1).id();

    return new CursorPageResponseIndexInfoDto(
        content,
        nextIdAfter,
        size,
        page.getTotalElements(),
        page.hasNext()
    );
  }

  public List<IndexInfoSummaryDto> findSummaryList() {
    return indexInfoRepository.findAll()
        .stream()
        .map(indexInfoMapper::toSummaryDto)
        .toList();
  }

  @Transactional
  public void delete(Long id) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(IndexInfoNotFoundException::new);
    indexDataRepository.deleteByIndexInfo(indexInfo);
    indexInfoRepository.deleteById(id);
  }
}
