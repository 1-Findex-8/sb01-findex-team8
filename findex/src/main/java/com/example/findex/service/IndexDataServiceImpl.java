package com.example.findex.service;

import com.example.findex.dto.response.IndexPerformanceDto;
import com.example.findex.entity.IndexData;
import com.example.findex.entity.IndexInfo;
import com.example.findex.repository.IndexDataRepository;
import com.example.findex.repository.IndexInfoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IndexDataServiceImpl{

  private final IndexDataRepository indexDataRepository;
  private final IndexInfoRepository indexInfoRepository;

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
}
