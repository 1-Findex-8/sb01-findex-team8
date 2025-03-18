package com.example.findex.service;

import com.example.findex.dto.syncjobs.response.GetStockMarketIndexResponse;
import com.example.findex.dto.syncjobs.response.SyncJobsDto;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

  public List<SyncJobsDto> syncIndexInfos() {
    GetStockMarketIndexResponse response = getStockMarketIndexResponse();
    // 실행 순서
    // 1. 200 개씩 받아온다.
      // 보통 1일당 156개
    // 2. index info로 가공해서 저장한다.
    // 3. sync jobs도 함께 저장
    return null;
  }

  public GetStockMarketIndexResponse getStockMarketIndexResponse(){
    String url = BASE_URL + STOCK_MARKET_INDEX
        + "?serviceKey=" + encodingServiceKey
        + "&resultType=json"
        + "&numOfRows=200"
        + "&basDt=20250313";
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
