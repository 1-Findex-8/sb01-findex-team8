package com.example.findex.service;

import com.example.findex.dto.indexdata.data.IndexDataDto;
import com.example.findex.dto.indexdata.request.IndexDataCreateRequest;
import com.example.findex.dto.indexdata.response.ChartDataPoint;
import com.example.findex.dto.indexdata.response.IndexChartDto;
import com.example.findex.dto.indexdata.response.IndexPerformanceDto;
import com.example.findex.dto.indexdata.response.RankedIndexPerformanceDto;
import com.example.findex.entity.IndexData;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.SourceType;
import com.example.findex.global.error.exception.IndexInfo.IndexInfoNotFoundException;
import com.example.findex.mapper.IndexDataMapper;
import com.example.findex.repository.IndexDataRepository;
import com.example.findex.repository.IndexInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional(readOnly = true)
  public List<IndexPerformanceDto> getInterestIndexPerformance(String periodType) {
    // 관심 종목 조회 (favorite=true)
    List<IndexInfo> favoriteIndexes = indexInfoRepository.findByFavorite(true);

    // 비교할 기준 날짜 설정
    LocalDate beforeDate = calculateStartDate(periodType);
    LocalDate today = LocalDate.now();

    // 관심 종목의 ID 리스트 추출
    List<Long> indexInfoIds = favoriteIndexes.stream()
        .map(IndexInfo::getId)
        .toList();

    // **한 번의 쿼리로 두 날짜의 데이터를 모두 가져오기**
    List<IndexData> indexDataList = indexDataRepository.findByIndexInfoIdInAndBaseDateIn(indexInfoIds, List.of(beforeDate, today));

    // 날짜별로 데이터를 맵핑 (Map<IndexInfoId, IndexData>)
    Map<Long, IndexData> beforeDataMap = indexDataList.stream()
        .filter(data -> data.getBaseDate().equals(beforeDate))
        .collect(Collectors.toMap(data -> data.getIndexInfo().getId(), Function.identity()));

    Map<Long, IndexData> currentDataMap = indexDataList.stream()
        .filter(data -> data.getBaseDate().equals(today))
        .collect(Collectors.toMap(data -> data.getIndexInfo().getId(), Function.identity()));

    // 관심 종목 데이터를 매칭하여 IndexPerformanceDto 생성
    return favoriteIndexes.stream()
        .map(indexInfo -> createIndexPerformanceDto(indexInfo, beforeDataMap, currentDataMap))
        .flatMap(Optional::stream)
        .collect(Collectors.toList());
  }

  private LocalDate calculateStartDate(String periodType) {
    LocalDate today = LocalDate.now();
    return switch (periodType) {
      case "DAILY" -> today;
      case "WEEKLY" -> today.minusWeeks(1);
      case "MONTHLY" -> today.minusMonths(1);
      default -> throw new IllegalStateException("Unexpected value: " + periodType);
    };
  }

  private Optional<IndexPerformanceDto> createIndexPerformanceDto(IndexInfo indexInfo, Map<Long, IndexData> beforeDataMap, Map<Long, IndexData> currentDataMap) {
    IndexData beforeData = beforeDataMap.get(indexInfo.getId());
    IndexData currentData = currentDataMap.get(indexInfo.getId());

    if (beforeData != null && currentData != null) {
      double beforePrice = beforeData.getClosingPrice().doubleValue();
      double currentPrice = currentData.getClosingPrice().doubleValue();
      double versus = currentPrice - beforePrice;
      double fluctuationRate = (versus / beforePrice) * 100.0;

      return Optional.of(new IndexPerformanceDto(
          indexInfo.getId().intValue(),
          indexInfo.getIndexClassification(),
          indexInfo.getIndexName(),
          versus,
          fluctuationRate,
          currentPrice,
          beforePrice
      ));
    }
    return Optional.empty();
  }

  public List<RankedIndexPerformanceDto> getIndexPerformanceRank(String periodType, int indexInfoId, int limit) {

    LocalDate beforeDate = calculateStartDate(periodType);
    LocalDate today = LocalDate.now();

    IndexInfo targetIndexInfo = indexInfoRepository.findById((long) indexInfoId)
        .orElseThrow(IndexInfoNotFoundException::new);

    List<IndexInfo> indexInfoList = indexInfoRepository.findByIndexClassification(targetIndexInfo.getIndexClassification());

    // 모든 IndexInfo의 IndexData를 한 번의 쿼리로 조회
    List<IndexData> indexDataList = indexDataRepository.findByIndexInfoInAndBaseDateIn(indexInfoList, List.of(beforeDate, today));

    // 날짜별로 데이터를 매핑
    Map<Long, IndexData> beforeDataMap = indexDataList.stream()
        .filter(data -> data.getBaseDate().equals(beforeDate))
        .collect(Collectors.toMap(data -> data.getIndexInfo().getId(), Function.identity()));

    Map<Long, IndexData> currentDataMap = indexDataList.stream()
        .filter(data -> data.getBaseDate().equals(today))
        .collect(Collectors.toMap(data -> data.getIndexInfo().getId(), Function.identity()));

    // IndexPerformanceDto 생성 및 정렬
    List<IndexPerformanceDto> performanceList = indexInfoList.stream()
        .map(indexInfo -> createIndexPerformanceDto(indexInfo, beforeDataMap, currentDataMap))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .sorted(Comparator.comparingDouble(IndexPerformanceDto::fluctuationRate).reversed()) // 등락률 기준 정렬
        .limit(limit)
        .toList();

    // rank 추가 후 RankedIndexPerformanceDto로 변환
    List<RankedIndexPerformanceDto> rankedList = IntStream.range(0, performanceList.size())
        .mapToObj(i -> new RankedIndexPerformanceDto(performanceList.get(i), i + 1))
        .limit(limit)
        .collect(Collectors.toList());

    return rankedList;
  }

  public IndexChartDto getIndexChart(String periodType, int indexInfoId, int limit) {
    LocalDate beforeDate = calculateStartDate(periodType);
    LocalDate today = LocalDate.now();

    // 해당 indexInfoId의 지수 정보 가져오기
    IndexInfo indexInfo = indexInfoRepository.findById((long) indexInfoId)
        .orElseThrow(IndexInfoNotFoundException::new);

    // 기간 내의 데이터 조회
    List<IndexData> indexDataList = indexDataRepository
        .findByIndexInfoAndBaseDateBetweenOrderByBaseDateAsc(indexInfo, beforeDate, today);

    // 차트 데이터 변환
    List<ChartDataPoint> dataPoints = indexDataList.stream()
        .map(data -> new ChartDataPoint(data.getBaseDate(), data.getClosingPrice().doubleValue()))
        .toList();

    // 이동 평균 데이터 계산
    List<ChartDataPoint> ma5DataPoints = calculateMovingAverage(dataPoints, 5);
    List<ChartDataPoint> ma20DataPoints = calculateMovingAverage(dataPoints, 20);

    return new IndexChartDto(
        indexInfoId,
        indexInfo.getIndexClassification(),
        indexInfo.getIndexName(),
        periodType,
        dataPoints,
        ma5DataPoints,
        ma20DataPoints
    );
  }

  private List<ChartDataPoint> calculateMovingAverage(List<ChartDataPoint> dataPoints, int period) {
    List<ChartDataPoint> maDataPoints = new ArrayList<>();
    for (int i = period - 1; i < dataPoints.size(); i++) {
      double sum = 0;
      for (int j = i - period + 1; j <= i; j++) {
        sum += dataPoints.get(j).value();
      }
      double avg = sum / period;
      maDataPoints.add(new ChartDataPoint(dataPoints.get(i).date(), avg));
    }
    return maDataPoints;
  }
}
