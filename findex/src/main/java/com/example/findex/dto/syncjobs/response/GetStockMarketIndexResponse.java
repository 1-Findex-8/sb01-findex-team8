package com.example.findex.dto.syncjobs.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetStockMarketIndexResponse(
    @JsonProperty("response") Response response
) {

}
