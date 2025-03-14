package com.example.findex.service;

import com.example.findex.dto.syncjobs.response.GetStockMarketIndexResponse;
import java.net.URI;
import java.net.URISyntaxException;
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

  public GetStockMarketIndexResponse getStockMarketIndexResponse() throws URISyntaxException {
    String url = BASE_URL + STOCK_MARKET_INDEX
        + "?serviceKey=" + encodingServiceKey
        + "&resultType=json"
        + "&numOfRows=200"
        + "&basDt=20250313";
    URI uri = new URI(url);


//    URI uri12= UriComponentsBuilder.fromUriString(BASE_URL + STOCK_MARKET_INDEX)
//        .queryParam("numOfRows", "200")
//        .queryParam("basDt", "20250313")
//        .queryParam("resultType", "json")
//        .queryParam("serviceKey", encodingServiceKey) // encoding serviceKey 추가
//        .build()
//        .toUri();

    GetStockMarketIndexResponse response = restTemplate.getForObject(uri, GetStockMarketIndexResponse.class);
    return response;
  }


}
