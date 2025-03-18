package com.example.findex.service;

import com.example.findex.dto.syncjobs.response.GetStockMarketIndexResponse;
import com.example.findex.dto.syncjobs.response.Item;
import com.example.findex.dto.syncjobs.response.SyncJobsDto;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.JobType;
import com.example.findex.entity.Result;
import com.example.findex.entity.SourceType;
import com.example.findex.entity.SyncJobs;
import com.example.findex.mapper.SyncJobsMapper;
import com.example.findex.repository.IndexInfoRepository;
import com.example.findex.repository.SyncJobRepository;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SyncJobsService {

  private static final String BASE_URL = "https://apis.data.go.kr/1160100/service/GetMarketIndexInfoService";
  private static final String STOCK_MARKET_INDEX = "/getStockMarketIndex";

  @Value("${open-api.encoding-service-key}")
  private String encodingServiceKey;
  private final RestTemplate restTemplate;

  private final SyncJobRepository syncJobRepository;
  private final IndexInfoRepository indexInfoRepository;

  private final SyncJobsMapper syncJobsMapper;

  public List<SyncJobsDto> syncIndexInfos() {
    // 실행 순서
    // 1. 200 개씩 받아온다.
      // 보통 1일당 156개
    GetStockMarketIndexResponse apiResponse = getStockMarketIndexResponse();

    // 2. index info로 가공해서 저장한다.
    List<SyncJobs> syncJobsList = new ArrayList<>();

    List<Item> itemList = apiResponse.response().body().items().item();
    itemList.forEach(
        item -> {
          IndexInfo indexInfo = indexInfoRepository.save(
              new IndexInfo(
                  item.idxCsf(),
                  item.idxNm(),
                  item.epyItmsCnt(),
                  LocalDate.parse(item.basPntm(), DateTimeFormatter.ofPattern("yyyyMMdd")),
                  BigDecimal.valueOf(item.basIdx()),
                  SourceType.OPEN_API,
                  false
              )
          );

          // 3. sync jobs도 함께 저장
          boolean exist = indexInfoRepository.existsById(indexInfo.getId());

          SyncJobs syncJobs = syncJobRepository.save(
              new SyncJobs(
                  JobType.INDEX_INFO,
                  null,
                  "system",
                  LocalDateTime.now(),
                  exist ? Result.SUCCESS : Result.FAILED,
                  indexInfo
              )
          );
          syncJobsList.add(syncJobs);
        }
    );
    return syncJobsMapper.toSyncJobsDtoList(syncJobsList);
  }

  public GetStockMarketIndexResponse getStockMarketIndexResponse(){
    String url = BASE_URL + STOCK_MARKET_INDEX
        + "?serviceKey=" + encodingServiceKey
        + "&resultType=json"
        + "&numOfRows=200"
        + "&basDt=" + getBaseDate();
    URI uri = URI.create(url);

    return restTemplate.getForObject(uri, GetStockMarketIndexResponse.class);
  }

  private String getBaseDate() {
    LocalDate today = LocalDate.now();

    // 전날을 구합니다.
    LocalDate yesterday = today.minusDays(1);

    // 날짜를 "yyyyMMdd" 형식으로 변환합니다.
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    return yesterday.format(formatter);
  }
}
