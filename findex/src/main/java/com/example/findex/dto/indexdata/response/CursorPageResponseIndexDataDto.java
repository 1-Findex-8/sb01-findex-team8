package com.example.findex.dto.indexdata.response;

import com.example.findex.dto.indexdata.data.IndexDataDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "커서 기반 페이지 응답")
public record CursorPageResponseIndexDataDto(
    @Schema(description = "페이지 내용")
    List<IndexDataDto> content, // 페이지 내용

    @Schema(description = "다음 페이지 커서",example = "eyJpZCI6MjB9")
    String nextCursor, // 다음 페이지 커서

    @Schema(description = "마지막 요소의 ID",example = "eyJpZCI6MjB9")
    Long nextIdAfter, // 마지막 요소의 ID

    @Schema(description = "페이지 크기",example = "10")
    int size, // 페이지 크기

    @Schema(description = "총 요소 수",example = "100")
    Long totalElements, // 총 요소 수

    @Schema(description = "다음 페이지 여부",example = "true")
    boolean hasNext // 다음 페이지 여부
) {

}
