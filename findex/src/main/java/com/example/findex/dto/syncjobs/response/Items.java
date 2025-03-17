package com.example.findex.dto.syncjobs.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Items(
    @JsonProperty("item") List<Item> item
){

}
