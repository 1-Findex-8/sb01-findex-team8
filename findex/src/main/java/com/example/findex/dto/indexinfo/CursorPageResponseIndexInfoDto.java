package com.example.findex.dto.indexinfo;

import java.util.List;

public record CursorPageResponseIndexInfoDto (
  List<IndexInfoDto> content, // 페이지 내용

  Long nextIdAfter, // 마지막 요소의 ID

  int size, // 페이지 크기

  Long totalElements, // 총 요소 수

  boolean hasNext) {
}
