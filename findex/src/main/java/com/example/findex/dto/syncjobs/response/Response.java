package com.example.findex.dto.syncjobs.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Response(
    @JsonProperty("header") Header header,
    @JsonProperty("body") Body body
) {

}
