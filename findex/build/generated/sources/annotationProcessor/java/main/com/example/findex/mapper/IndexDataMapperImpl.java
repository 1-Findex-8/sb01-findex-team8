package com.example.findex.mapper;

import com.example.findex.dto.indexdata.data.IndexDataDto;
import com.example.findex.entity.IndexData;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.SourceType;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-19T13:45:48+0900",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.2.jar, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class IndexDataMapperImpl implements IndexDataMapper {

    @Override
    public IndexDataDto toDto(IndexData indexData) {
        if ( indexData == null ) {
            return null;
        }

        Long indexInfoId = null;
        Long id = null;
        LocalDate baseDate = null;
        SourceType sourceType = null;
        BigDecimal marketPrice = null;
        BigDecimal closingPrice = null;
        BigDecimal highPrice = null;
        BigDecimal lowPrice = null;
        BigDecimal versus = null;
        BigDecimal fluctuationRate = null;
        Long tradingQuantity = null;
        Long tradingPrice = null;
        Long marketTotalAmount = null;

        indexInfoId = indexDataIndexInfoId( indexData );
        id = indexData.getId();
        baseDate = indexData.getBaseDate();
        sourceType = indexData.getSourceType();
        marketPrice = indexData.getMarketPrice();
        closingPrice = indexData.getClosingPrice();
        highPrice = indexData.getHighPrice();
        lowPrice = indexData.getLowPrice();
        versus = indexData.getVersus();
        fluctuationRate = indexData.getFluctuationRate();
        tradingQuantity = indexData.getTradingQuantity();
        tradingPrice = indexData.getTradingPrice();
        marketTotalAmount = indexData.getMarketTotalAmount();

        IndexDataDto indexDataDto = new IndexDataDto( id, indexInfoId, baseDate, sourceType, marketPrice, closingPrice, highPrice, lowPrice, versus, fluctuationRate, tradingQuantity, tradingPrice, marketTotalAmount );

        return indexDataDto;
    }

    private Long indexDataIndexInfoId(IndexData indexData) {
        IndexInfo indexInfo = indexData.getIndexInfo();
        if ( indexInfo == null ) {
            return null;
        }
        return indexInfo.getId();
    }
}
