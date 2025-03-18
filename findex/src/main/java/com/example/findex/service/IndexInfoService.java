package com.example.findex.service;

import com.example.findex.dto.indexinfo.CreateIndexInfoRequest;
import com.example.findex.dto.indexinfo.FindIndexInfoRequest;
import com.example.findex.dto.indexinfo.IndexInfoDto;
import com.example.findex.dto.indexinfo.SortDirectionType;
import com.example.findex.dto.indexinfo.SortFieldType;
import com.example.findex.dto.indexinfo.UpdateIndexInfoRequest;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.SourceType;
import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;
import com.example.findex.mapper.IndexInfoMapper;
import com.example.findex.repository.IndexInfoRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexInfoService {

  private final IndexInfoRepository indexInfoRepository;
  private final IndexInfoMapper indexInfoMapper;

  public IndexInfoDto create(CreateIndexInfoRequest request, SourceType sourceType, boolean favorite) {

    if (indexInfoRepository.existsByIndexClassificationAndIndexName(request.indexClassification(), request.indexName())) {
      throw new BusinessException(ErrorCode.INDEX_INFO_DUPLICATE_EXCEPTION);
    }

    IndexInfo indexInfo = new IndexInfo(
        request.indexClassification(),
        request.indexName(),
        request.employedItemsCount(),
        request.basePointInTime(),
        request.baseIndex(),
        sourceType,
        favorite);

    return indexInfoMapper.toDto(indexInfoRepository.save(indexInfo));
  }

  public IndexInfoDto update(Long indexInfoId, UpdateIndexInfoRequest request) {
    IndexInfo indexInfo = indexInfoRepository.findById(indexInfoId).orElseThrow(() -> new BusinessException(ErrorCode.INDEX_INFO_NOT_FOUND));

    // TODO: update 로직
    return indexInfoMapper.toDto(indexInfoRepository.save(indexInfo));
  }

  public IndexInfoDto findById(Long indexInfoId) {
    return indexInfoMapper.toDto(
        indexInfoRepository.findById(indexInfoId)
            .orElseThrow(() -> new BusinessException(ErrorCode.INDEX_INFO_NOT_FOUND)));
  }

  public List<IndexInfoDto> findAndSort(FindIndexInfoRequest request) {
    // TODO: 수정) 조회 조건이 여러 개인 경우 모든 조건을 만족한 결과로 조회합니다.

    Set<IndexInfoDto> infoSet = indexInfoRepository.findByFavorite(request.favorite()).stream().map(
        indexInfoMapper::toDto).collect(
        Collectors.toSet());

    if (request.indexClassification() == null) {
      infoSet.addAll(indexInfoRepository.findAll().stream().map(indexInfoMapper::toDto).collect(Collectors.toSet()));
    } else {
      infoSet.addAll(indexInfoRepository.findByIndexClassificationContaining(
          request.indexClassification()).stream().map(indexInfoMapper::toDto).collect(Collectors.toSet()));
    }

    if (request.indexName() == null) {
      infoSet.addAll(indexInfoRepository.findAll().stream().map(indexInfoMapper::toDto).collect(Collectors.toSet()));
    } else {
      infoSet.addAll(indexInfoRepository.findByIndexNameContaining(request.indexName()).stream().map(indexInfoMapper::toDto).collect(
        Collectors.toSet()));
    }

    List<IndexInfoDto> infoList = new ArrayList<>(infoSet.stream().toList());

    if (request.sortField().equals(SortFieldType.indexClassification)) {
      if (request.sortDirection().equals(SortDirectionType.asc)) {
        infoList.sort((Comparator.comparing(IndexInfoDto::indexClassification)));
      }
      else if (request.sortDirection().equals(SortDirectionType.desc)) {
        infoList.sort((Comparator.comparing(IndexInfoDto::indexClassification).reversed()));
      }
    }

    if (request.sortField().equals(SortFieldType.indexName)) {
      if (request.sortDirection().equals(SortDirectionType.asc)) {
        infoList.sort((Comparator.comparing(IndexInfoDto::indexName)));
      }
      else if (request.sortDirection().equals(SortDirectionType.desc)) {
        infoList.sort((Comparator.comparing(IndexInfoDto::indexName).reversed()));
      }
    }

    if (request.sortField().equals(SortFieldType.employedItemsCount)) {
      if (request.sortDirection().equals(SortDirectionType.asc)) {
        infoList.sort((Comparator.comparing(IndexInfoDto::employeeItemsCount)));
      }
      else if (request.sortDirection().equals(SortDirectionType.desc)) {
        infoList.sort((Comparator.comparing(IndexInfoDto::employeeItemsCount).reversed()));
      }
    }

    // TODO: 페이지네이션 추가

    return infoList;
  }

}
