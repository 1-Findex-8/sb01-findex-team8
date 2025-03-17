package com.example.findex.mapper;

import com.example.findex.dto.indexdata.data.IndexDataDto;
import com.example.findex.entity.IndexData;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.SourceType;
import com.example.findex.repository.IndexInfoRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class IndexDataMapper {
  private final IndexInfoRepository indexInfoRepository;

  public IndexDataDto toDto(IndexData indexData) {
    IndexInfo indexInfo = indexInfoRepository.findById(indexData.getId()).orElseThrow(()->new NoSuchElementException("IndexInfo with id "+indexData.getId()+ "not found"));

    return new IndexDataDto(
        indexData.getId(),
        indexInfo.getId(),
        indexData.getBaseDate(),
        SourceType.USER,
        indexData.getMarketPrice(),
        indexData.getClosingPrice(),
        indexData.getHighPrice(),
        indexData.getLowPrice(),
        indexData.getVersus(),
        indexData.getFluctuationRate(),
        indexData.getTradingQuantity(),
        indexData.getTradingPrice(),
        indexData.getMarketTotalAmount()
    );
  }

}
