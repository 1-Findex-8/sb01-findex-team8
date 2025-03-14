package com.example.findex.service;

import com.example.findex.dto.data.IndexDataDto;
import com.example.findex.dto.request.IndexDataCreateRequest;
import com.example.findex.entity.IndexData;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.SourceType;
import com.example.findex.mapper.IndexDataMapper;
import com.example.findex.repository.IndexDataRepository;
import com.example.findex.repository.IndexInfoRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexDataServiceImpl implements IndexDataService {

  private final IndexDataRepository indexDataRepository;
  private final IndexInfoRepository indexInfoRepository;
  private final IndexDataMapper indexDataMapper;


  //지수 데이터 생성
  @Override
  public IndexDataDto create(IndexDataCreateRequest request) {
    //중복 체크
    if (indexDataRepository.existsByIndexInfoIdAndBaseDate(request.indexInfoId(),request.baseDate())) {
      throw new DataIntegrityViolationException("지수 및 날짜 조합이 이미 존재합니다.");
    }
    IndexInfo indexInfo = indexInfoRepository.findById(request.indexInfoId()).orElseThrow(()->new NoSuchElementException("IndexInfo with id "+request.indexInfoId()+ "not found"));

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
}
