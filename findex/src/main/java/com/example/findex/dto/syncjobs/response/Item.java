package com.example.findex.dto.syncjobs.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Item(
    @JsonProperty("basDt") String basDt,
    @JsonProperty("idxNm") String idxNm,
    @JsonProperty("idxCsf") String idxCsf,
    @JsonProperty("epyItmsCnt") int epyItmsCnt,
    @JsonProperty("clpr") double clpr,
    @JsonProperty("vs") double vs,
    @JsonProperty("fltRt") double fltRt,
    @JsonProperty("mkp") double mkp,
    @JsonProperty("hipr") double hipr,
    @JsonProperty("lopr") double lopr,
    @JsonProperty("trqu") long trqu,
    @JsonProperty("trPrc") long trPrc,
    @JsonProperty("lstgMrktTotAmt") long lstgMrktTotAmt,
    @JsonProperty("lsYrEdVsFltRg") int lsYrEdVsFltRg,
    @JsonProperty("lsYrEdVsFltRt") double lsYrEdVsFltRt,
    @JsonProperty("yrWRcrdHgst") double yrWRcrdHgst,
    @JsonProperty("yrWRcrdHgstDt") String yrWRcrdHgstDt,
    @JsonProperty("yrWRcrdLwst") double yrWRcrdLwst,
    @JsonProperty("yrWRcrdLwstDt") String yrWRcrdLwstDt,
    @JsonProperty("basPntm") String basPntm,
    @JsonProperty("basIdx") double basIdx
) {

}
