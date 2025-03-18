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
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
  public List<SyncJobsDto> syncIndexInfos(HttpServletRequest httpServletRequest) {
    // 실행 순서
    // 1. 200 개씩 받아온다.
      // 보통 1일당 156개
    GetStockMarketIndexResponse apiResponse = getStockMarketIndexResponse();

    // 2. index info로 가공해서 저장한다.
    List<SyncJobs> syncJobsList = new ArrayList<>();

    List<Item> itemList = apiResponse.response().body().items().item();
    itemList.forEach(
        item -> {
          IndexInfo indexInfo = getIndexInfo(item);

          // 3. sync jobs도 함께 저장
          boolean exist = indexInfoRepository.existsById(indexInfo.getId());
          String worker = getIp(httpServletRequest);

          SyncJobs syncJobs = syncJobRepository.save(
              new SyncJobs(
                  JobType.INDEX_INFO,
                  null,
                  worker,
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
  public List<SyncJobsDto> syncIndexData(
      IndexDataSyncRequest request, HttpServletRequest httpServletRequest) {
    if (request.indexInfoIds().isEmpty()) return new ArrayList<>();

    // 실행 순서
    // 1. indexInfoId 로 조회
    List<IndexInfo> indexInfos = request.indexInfoIds().stream()
        .map(indexInfoId -> indexInfoRepository.findById(indexInfoId)
              .orElseThrow(() -> new NoSuchElementException("IndexInfo를 찾지 못했습니다.")) // 추후 변경
        )
        .toList();

    // 2. 조회 결과를 바탕으로 index name 추출 후 api 요청
    String worker = getIp(httpServletRequest);
    List<SyncJobs> syncJobsList = new ArrayList<>();
    indexInfos.forEach(
      indexInfo -> syncJobsList.addAll(callApi(indexInfo, request, worker))
    );

    return syncJobsMapper.toSyncJobsDtoList(syncJobsList);
  }

  private IndexInfo getIndexInfo(Item item) {
    Optional<IndexInfo> indexInfoOptional =
        indexInfoRepository.findByIndexClassificationAndIndexName(item.idxCsf(), item.idxNm());

    if (indexInfoOptional.isPresent()) { // indexInfo 존재 시 수정
      IndexInfo indexInfo = indexInfoOptional.get();

      indexInfo.updateBaseIndex(BigDecimal.valueOf(item.basIdx()));
      indexInfo.updateBasePointInTime(getLocalDate(item.basPntm()));
      indexInfo.updateEmployeeItemsCount(item.epyItmsCnt());

      return indexInfoRepository.save(indexInfo);
    }

    // 기존 indexInfo 가 없다면 새로 저장
    return indexInfoRepository.save(
        new IndexInfo(
            item.idxCsf(),
            item.idxNm(),
            item.epyItmsCnt(),
            getLocalDate(item.basPntm()),
            BigDecimal.valueOf(item.basIdx()),
            SourceType.OPEN_API,
            false
        )
    );

  }

  private List<SyncJobs> callApi(IndexInfo indexInfo, IndexDataSyncRequest request, String worker) {
    String indexName = indexInfo.getIndexName();
    LocalDate baseDateFrom = request.baseDateFrom();
    LocalDate baseDateTo = request.baseDateTo();

    List<Item> items = new ArrayList<>();

    int currentCount = 0;
    int pageNum = 1;
    GetStockMarketIndexResponse response;
    do { // indexName에 대한 모든 정보를 받아올 때까지 계속 요청
      response = getStockMarketIndexResponse(indexName, baseDateFrom, baseDateTo, pageNum);
      items.addAll(response.response().body().items().item());
      currentCount += items.size();
      pageNum++;
    } while (currentCount != response.response().body().totalCount());

    // 3. indexData, syncjob 저장
    return items.stream()
        .map(item -> saveIndexDataAndSyncJobs(indexInfo, item, worker))
        .toList();
  }

  private SyncJobs saveIndexDataAndSyncJobs(IndexInfo indexInfo, Item item, String worker) {
    IndexData indexData = indexDataRepository.save(
        new IndexData(
            indexInfo,
            getLocalDate(item.basDt()),
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
            worker,
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

  private LocalDate getLocalDate(String date) {
    return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
  }

  private String getIp(HttpServletRequest request) {
    List<String> headerList = Arrays.asList(
        "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
        "HTTP_X_FORWARDED_FOR");
    String ip = null;

    for (String header : headerList) {
      if (ip == null) {
        ip = request.getHeader(header);
      }
    }

    if (ip == null) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }
}
