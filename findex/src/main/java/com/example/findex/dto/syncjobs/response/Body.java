package com.example.findex.dto.syncjobs.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Body(
    @JsonProperty("numOfRows") int numOfRows,
    @JsonProperty("pageNo") int pageNo,
    @JsonProperty("totalCount") int totalCount,
    @JsonProperty("items") Items items
) {

}
