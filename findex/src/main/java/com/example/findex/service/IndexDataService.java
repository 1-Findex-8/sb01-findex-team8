package com.example.findex.service;

import com.example.findex.dto.indexdata.data.IndexDataDto;
import com.example.findex.dto.indexdata.request.IndexDataCreateRequest;
import com.example.findex.dto.indexdata.response.CursorPageResponseIndexDataDto;
import com.example.findex.entity.IndexData;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.SourceType;
import com.example.findex.mapper.IndexDataMapper;
import com.example.findex.repository.IndexDataRepository;
import com.example.findex.repository.IndexInfoRepository;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexDataService {

  private final IndexDataRepository indexDataRepository;
  private final IndexInfoRepository indexInfoRepository;
  private final IndexDataMapper indexDataMapper;

  //지수 데이터 생성
  public IndexDataDto create(IndexDataCreateRequest request) {
    //중복 체크
    if (indexDataRepository.existsByIndexInfoIdAndBaseDate(request.indexInfoId(),request.baseDate())) {
      throw new DataIntegrityViolationException("지수 및 날짜 조합이 이미 존재합니다.");
    }
    IndexInfo indexInfo = indexInfoRepository.findById(request.indexInfoId())
        .orElseThrow(()->new NoSuchElementException("IndexInfo with id "+request.indexInfoId()+ "not found"));

    //사용자가 생성
    IndexData indexData = new IndexData(
        indexInfo,
        request.baseDate(),
        SourceType.USER,
        request.marketPrice(),
        request.closingPrice(),
        request.highPrice(),
        request.lowPrice(),
        request.versus(),
        request.fluctuationRate(),
        request.tradingQuantity(),
        request.tradingPrice(),
        request.marketTotalAmount()
    );
    IndexData savedIndexData = indexDataRepository.save(indexData);
    return indexDataMapper.toDto(savedIndexData);
  }

  //지수 데이터 조회(커서 페이징네이션)
  public CursorPageResponseIndexDataDto findIndexDataList(long indexInfoId, LocalDate startDate, LocalDate endDate, long idAfter,String cursor, String sortField,
      String sortDirection, int size) {
    // 커서 기반 페이지네이션과 정렬 로직 처리
    Pageable pageable = PageRequest.of(0, size,
        sortDirection.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

    // 데이터 조회
    Page<IndexData> indexDataPage = indexDataRepository.findIndexDataList(
        indexInfoId, startDate, endDate, idAfter, pageable
    );

    // DTO 변환
    List<IndexDataDto> content = indexDataPage.getContent().stream()
        .map(indexDataMapper::toDto)
        .collect(Collectors.toList());

    // 커서 계산
    String nextCursor = null;
    if (indexDataPage.hasNext()) {
      nextCursor = generateNextCursor(indexDataPage.getContent().get(indexDataPage.getContent().size() - 1).getId());
    }

    return new CursorPageResponseIndexDataDto(
        content,
        nextCursor,
        indexDataPage.getContent().isEmpty() ? null : indexDataPage.getContent().get(indexDataPage.getContent().size() - 1).getId(),
        size,
        indexDataPage.getTotalElements(),
        indexDataPage.hasNext()
    );
  }
  private String generateNextCursor(Long lastId) {
    return Base64.getEncoder().encodeToString(lastId.toString().getBytes(StandardCharsets.UTF_8));
  }
}
