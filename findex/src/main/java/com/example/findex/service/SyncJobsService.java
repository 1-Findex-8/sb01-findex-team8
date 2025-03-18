package com.example.findex.service;

import com.example.findex.dto.syncjobs.request.IndexDataSyncRequest;
import com.example.findex.dto.syncjobs.response.GetStockMarketIndexResponse;
import com.example.findex.dto.syncjobs.response.Item;
import com.example.findex.dto.syncjobs.response.SyncJobsDto;
import com.example.findex.entity.IndexData;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.JobType;
import com.example.findex.entity.Result;
import com.example.findex.entity.SourceType;
import com.example.findex.entity.SyncJobs;
import com.example.findex.mapper.SyncJobsMapper;
import com.example.findex.repository.IndexDataRepository;
import com.example.findex.repository.IndexInfoRepository;
import com.example.findex.repository.SyncJobRepository;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
  private final IndexDataRepository indexDataRepository;

  private final SyncJobsMapper syncJobsMapper;

  @Transactional
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

  @Transactional
  public List<SyncJobsDto> syncIndexData(IndexDataSyncRequest request) {
    // 실행 순서
    // 1. indexInfoId 로 조회
    List<IndexInfo> indexInfos = request.indexInfoIds().stream()
        .map(indexInfoId -> indexInfoRepository.findById(indexInfoId)
              .orElseThrow(() -> new NoSuchElementException("IndexInfo를 찾지 못했습니다.")) // 추후 변경
        )
        .toList();

    // 2. 조회 결과를 바탕으로 index name 추출 후 api 요청
    List<SyncJobs> syncJobsList = new ArrayList<>();
    indexInfos.forEach(
      indexInfo -> syncJobsList.addAll(callApi(indexInfo, request))
    );

    return syncJobsMapper.toSyncJobsDtoList(syncJobsList);
  }

  private List<SyncJobs> callApi(IndexInfo indexInfo, IndexDataSyncRequest request) {
    String indexName = indexInfo.getIndexName();
    LocalDate baseDateFrom = request.baseDateFrom();
    LocalDate baseDateTo = request.baseDateTo();

    List<Item> items = new ArrayList<>();

    int currentCount = 0;
    int pageNum = 1;
    GetStockMarketIndexResponse response = null;
    do { // indexName에 대한 모든 정보를 받아올 때까지 계속 요청
      response = getStockMarketIndexResponse(indexName, baseDateFrom, baseDateTo, pageNum);
      items.addAll(response.response().body().items().item());
      currentCount += items.size();
      pageNum++;
    } while (currentCount != response.response().body().totalCount());

    // 3. indexData, syncjob 저장
    List<SyncJobs> syncJobsList = items.stream()
        .map(item -> saveIndexDataAndSyncJobs(indexInfo, item))
        .toList();

    return syncJobsList;
  }

  private SyncJobs saveIndexDataAndSyncJobs(IndexInfo indexInfo, Item item) {
    IndexData indexData = indexDataRepository.save(
        new IndexData(
            indexInfo,
            LocalDate.parse(item.basDt(), DateTimeFormatter.ofPattern("yyyyMMdd")),
            SourceType.OPEN_API,
            BigDecimal.valueOf(item.mkp()),
            BigDecimal.valueOf(item.clpr()),
            BigDecimal.valueOf(item.hipr()),
            BigDecimal.valueOf(item.lopr()),
            BigDecimal.valueOf(item.vs()),
            BigDecimal.valueOf(item.fltRt()),
            item.trqu(),
            item.trPrc(),
            item.lstgMrktTotAmt()
        )
    );

    boolean exist = indexDataRepository.existsById(indexData.getId());

    return syncJobRepository.save(
        new SyncJobs(
            JobType.INDEX_DATA,
            indexData.getBaseDate(),
            "system",
            LocalDateTime.now(),
            exist ? Result.SUCCESS : Result.FAILED,
            indexInfo
        )
    );
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

  public GetStockMarketIndexResponse getStockMarketIndexResponse(
      String indexName, LocalDate baseDateFrom, LocalDate baseDataTo, int pageNum){
    String encodedIndexName = URLEncoder.encode(indexName, StandardCharsets.UTF_8);

    String url = BASE_URL + STOCK_MARKET_INDEX
        + "?serviceKey=" + encodingServiceKey
        + "&resultType=json"
        + "&numOfRows=1000"
        + "&pageNo=" + pageNum
        + "&idxNm=" + encodedIndexName
        + "&beginBasDt=" + formatLocalDate(baseDateFrom)
        + "&endBasDt=" + formatLocalDate(baseDataTo);
    URI uri = URI.create(url);

    return restTemplate.getForObject(uri, GetStockMarketIndexResponse.class);
  }

  private String getBaseDate() {
    LocalDate today = LocalDate.now();
    LocalDate yesterday = today.minusDays(1);
    return formatLocalDate(yesterday);
  }

  private String formatLocalDate(LocalDate localDate) {
    return localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
  }
}
