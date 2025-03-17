package com.example.findex.dto.syncjobs.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Header(
    @JsonProperty("resultCode") String resultCode,
    @JsonProperty("resultMsg") String resultMsg
) {

}
