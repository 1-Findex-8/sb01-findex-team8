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
import com.example.findex.global.error.exception.indexinfo.IndexInfoInvalidSortFieldException;
import com.example.findex.global.error.exception.indexinfo.IndexInfoNotFoundException;
import com.example.findex.mapper.IndexInfoMapper;
import com.example.findex.repository.IndexDataRepository;
import com.example.findex.repository.IndexInfoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IndexInfoService {

  private final IndexInfoRepository indexInfoRepository;
  private final IndexInfoMapper indexInfoMapper;
  private final IndexDataRepository indexDataRepository;

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
      SortDirectionType sortDirection,
      Integer size) {

    cursor = null;

//    // 커서값을 Long 타입으로 변환하여 검증
//    Long parsedCursor = null;
//
//    if (cursor != null) {
//      try {
//        parsedCursor = Long.parseLong(cursor);
//      } catch (NumberFormatException e) {
//        throw new IndexInfoInvalidCursorException("커서의 값이 적절하지 않습니다.");
//      }
//    }

//    // 커서값과 idAfter가 주어졌다면 비교하여 일관성 검증
//    if (cursor != null && idAfter != null && !idAfter.equals(parsedCursor)) {
//      throw new IndexInfoInvalidCursorException("커서의 값과 idAfter의 값이 같지 않습니다.");
//    }

    // 정렬 조건 설정
    Sort sort = Sort.by(getSortOrder(sortField, sortDirection));

    // 페이지네이션을 위해 커서 페이지네이션 사용 (idAfter 사용)
    Pageable pageable = PageRequest.of(0, size, sort);

    // 데이터 필터링 및 커서 페이지네이션을 위한 추가 조건 설정
    Page<IndexInfoDto> pageResult;

    pageResult = indexInfoRepository.findByFilters(
        indexClassification, indexName, favorite, idAfter, sortField, pageable)
        .map(indexInfoMapper::toDto);

//    // cursor 또는 idAfter에 따라 조건 추가
//    if (cursor != null || idAfter != null) {
//      pageResult = indexInfoRepository.findByFilters(indexClassification, indexName, favorite, idAfter, sortField, pageable);
//    } else {
//      pageResult = indexInfoRepository.findByFilters(indexClassification, indexName, favorite, null, sortField, pageable);
//    }

    // 다음 페이지 처리
    Long nextIdAfter = null;
//    String nextCursor = null;
    boolean hasNext = pageResult.hasNext();

    // 결과가 있을 경우 마지막 요소에 대한 처리
    if (!pageResult.isEmpty()) {
      IndexInfoDto lastItem = pageResult.getContent().get(pageResult.getContent().size() - 1);
      nextIdAfter = lastItem.id();  // 마지막 요소의 ID
//      nextCursor = String.valueOf(lastItem.id());  // 커서값 (ID를 문자열로 변환)
    }

//    // idAfter 값 검증 로직 추가
//    if (cursor != null && parsedCursor != null && !parsedCursor.equals(nextIdAfter)) {
//      throw new IllegalArgumentException("Cursor mismatch: The provided cursor does not match the last item of the current page.");
//    }

    return new CursorPageResponseIndexInfoDto(
        pageResult.getContent(),
        nextIdAfter,
        size,
        pageResult.getTotalElements(),
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
        throw new IndexInfoInvalidSortFieldException();
    }
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
