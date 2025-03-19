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
import java.util.Optional;
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
    IndexInfo indexInfo = indexInfoRepository.findById(indexInfoId)
        .orElseThrow(() -> new BusinessException(ErrorCode.INDEX_NOT_FOUND)); // 임시 수정

    indexInfo.setEmployeeItemsCount(request.employedItemsCount());
    indexInfo.setBasePointInTime(request.basePointInTime());
    indexInfo.setBaseIndex(request.baseIndex());
    indexInfo.setFavorite(request.favorite());

    return indexInfoMapper.toDto(indexInfoRepository.save(indexInfo));
  }

  public IndexInfoDto findById(Long indexInfoId) {
    return indexInfoMapper.toDto(
        indexInfoRepository.findById(indexInfoId)
            .orElseThrow(() -> new BusinessException(ErrorCode.INDEX_NOT_FOUND))); // 임시 수정
  }

  public List<IndexInfoDto> findAndSort(FindIndexInfoRequest request) {
    // 조건 필터링
    List<IndexInfoDto> infoList = new ArrayList<>(
        indexInfoRepository.findByFavorite(request.favorite())
            .stream()
            .map(indexInfoMapper::toDto)
            .toList()
    );

    List<IndexInfoDto> classificationList = Optional.ofNullable(request.indexClassification())
        .map(indexClassification -> new ArrayList<>(indexInfoRepository.findByIndexClassificationContaining(indexClassification)
            .stream()
            .map(indexInfoMapper::toDto)
            .toList()))
        .orElse(new ArrayList<>());

    List<IndexInfoDto> nameList = Optional.ofNullable(request.indexName())
        .map(indexName -> new ArrayList<>(indexInfoRepository.findByIndexNameContaining(indexName)
            .stream()
            .map(indexInfoMapper::toDto)
            .toList()))
        .orElse(new ArrayList<>());

    if (!classificationList.isEmpty() && !nameList.isEmpty()) {
      infoList.retainAll(classificationList);
      infoList.retainAll(nameList);
    }

    // 정렬
    if (request.sortField().equals(SortFieldType.indexClassification)) {
      if (request.sortDirection().equals(SortDirectionType.asc)) {
        infoList.sort((Comparator.comparing(IndexInfoDto::indexClassification)));
      } else if (request.sortDirection().equals(SortDirectionType.desc)) {
        infoList.sort((Comparator.comparing(IndexInfoDto::indexClassification).reversed()));
      }
    }

    if (request.sortField().equals(SortFieldType.indexName)) {
      if (request.sortDirection().equals(SortDirectionType.asc)) {
        infoList.sort((Comparator.comparing(IndexInfoDto::indexName)));
      } else if (request.sortDirection().equals(SortDirectionType.desc)) {
        infoList.sort((Comparator.comparing(IndexInfoDto::indexName).reversed()));
      }
    }

    if (request.sortField().equals(SortFieldType.employedItemsCount)) {
      if (request.sortDirection().equals(SortDirectionType.asc)) {
        infoList.sort((Comparator.comparing(IndexInfoDto::employeeItemsCount)));
      } else if (request.sortDirection().equals(SortDirectionType.desc)) {
        infoList.sort((Comparator.comparing(IndexInfoDto::employeeItemsCount).reversed()));
      }
    }

    // TODO: 페이지네이션 추가
    // 페이지네이션


    return infoList;
  }

}
