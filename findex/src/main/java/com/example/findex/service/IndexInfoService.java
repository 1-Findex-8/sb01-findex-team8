package com.example.findex.service;

import com.example.findex.dto.indexinfo.CreateIndexInfoRequest;
import com.example.findex.dto.indexinfo.CursorPageResponseIndexInfoDto;
import com.example.findex.dto.indexinfo.IndexInfoDto;
import com.example.findex.dto.indexinfo.SortDirectionType;
import com.example.findex.dto.indexinfo.UpdateIndexInfoRequest;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.SourceType;
import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.indexinfo.IndexInfoDuplicateException;
import com.example.findex.global.error.exception.indexinfo.IndexInfoInvalidCursor;
import com.example.findex.global.error.exception.indexinfo.IndexInfoInvalidSortField;
import com.example.findex.global.error.exception.indexinfo.IndexInfoNotFoundException;
import com.example.findex.mapper.IndexInfoMapper;
import com.example.findex.repository.IndexInfoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexInfoService {

  private final IndexInfoRepository indexInfoRepository;
  private final IndexInfoMapper indexInfoMapper;

  public IndexInfoDto create(CreateIndexInfoRequest request, SourceType sourceType, boolean favorite) {

    if (indexInfoRepository.existsByIndexClassificationAndIndexName(request.indexClassification(), request.indexName())) {
      throw new IndexInfoDuplicateException(ErrorCode.INDEX_INFO_DUPLICATE_EXCEPTION.getMessage());
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
        .orElseThrow(() -> new IndexInfoNotFoundException(ErrorCode.INDEX_INFO_NOT_FOUND.getMessage()));

    indexInfo.updateEmployeeItemsCount(request.employedItemsCount());
    indexInfo.updateBasePointInTime(request.basePointInTime());
    indexInfo.updateBaseIndex(request.baseIndex());
    indexInfo.updateFavorite(request.favorite());

    return indexInfoMapper.toDto(indexInfoRepository.save(indexInfo));
  }

  public IndexInfoDto findById(Long indexInfoId) {
    return indexInfoMapper.toDto(
        indexInfoRepository.findById(indexInfoId)
            .orElseThrow(() -> new IndexInfoNotFoundException(ErrorCode.INDEX_INFO_NOT_FOUND.getMessage())));
  }

  public CursorPageResponseIndexInfoDto getIndexInfoList(
      String indexClassification,
      String indexName,
      boolean favorite,
      Long idAfter,
      String cursor,
      String sortField,
      SortDirectionType sortDirection,
      int size) {

    // 커서값을 Long 타입으로 변환하여 검증
    Long parsedCursor = null;

    if (cursor != null) {
      try {
        parsedCursor = Long.parseLong(cursor);
      } catch (NumberFormatException e) {
        throw new IndexInfoInvalidCursor(ErrorCode.INDEX_INFO_INVALID_CURSOR.getMessage());
      }
    }

    // 커서값과 idAfter가 주어졌다면 비교하여 일관성 검증
    if (cursor != null && idAfter != null && !idAfter.equals(parsedCursor)) {
      throw new IndexInfoInvalidCursor(ErrorCode.INDEX_INFO_INVALID_CURSOR.getMessage());
    }

    // 정렬 조건 설정
    Sort sort = Sort.by(getSortOrder(sortField, sortDirection));

    // 페이지네이션을 위해 커서 페이지네이션 사용 (idAfter 사용)
    Pageable pageable = PageRequest.of(0, size, sort);

    // 데이터 필터링 및 커서 페이지네이션을 위한 추가 조건 설정
    List<IndexInfoDto> resultList;

    // cursor 또는 idAfter에 따라 조건 추가
    if (cursor != null || idAfter != null) {
      resultList = indexInfoRepository.findByFilters(indexClassification, indexName, favorite, idAfter, pageable);
    } else {
      resultList = indexInfoRepository.findByFilters(indexClassification, indexName, favorite, null, pageable);
    }

    // 다음 페이지 처리
    Long nextIdAfter = null;
    String nextCursor = null;
    boolean hasNext = false;

    // 결과가 있을 경우 마지막 요소에 대한 처리
    if (!resultList.isEmpty()) {
      IndexInfoDto lastItem = resultList.get(resultList.size() - 1);
      nextIdAfter = lastItem.id();  // 마지막 요소의 ID
      nextCursor = String.valueOf(lastItem.id());  // 커서값 (ID를 문자열로 변환)
    }

    // 다음 페이지가 있는지 여부 확인
    hasNext = resultList.size() == size;

    // idAfter 값 검증 로직 추가
    if (cursor != null && parsedCursor != null && !parsedCursor.equals(nextIdAfter)) {
      throw new IllegalArgumentException("Cursor mismatch: The provided cursor does not match the last item of the current page.");
    }

    return new CursorPageResponseIndexInfoDto(
        resultList,
        nextCursor,
        nextIdAfter,
        size,
        (long) resultList.size(), // totalElements
        hasNext
    );
  }

  private Sort.Order getSortOrder(String sortField, SortDirectionType sortDirection) {
    switch (sortField) {
      case "indexClassification":
        return sortDirection == SortDirectionType.asc
            ? Sort.Order.asc("indexClassification")
            : Sort.Order.desc("indexClassification");
      case "indexName":
        return sortDirection == SortDirectionType.asc
            ? Sort.Order.asc("indexName")
            : Sort.Order.desc("indexName");
      case "employedItemsCount":
        return sortDirection == SortDirectionType.asc
            ? Sort.Order.asc("employedItemsCount")
            : Sort.Order.desc("employedItemsCount");
      default:
        throw new IndexInfoInvalidSortField(ErrorCode.INDEX_INFO_INVALID_SORT_FIELD.getMessage());
    }
  }
}
