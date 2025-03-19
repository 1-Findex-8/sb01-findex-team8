package com.example.findex.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
  
  // index info
  INDEX_INFO_NOT_FOUND(404, "지수 정보를 찾지 못했습니다."),
  INDEX_INFO_DUPLICATE_EXCEPTION(400, "지수 정보가 중복입니다."),
  INDEX_INFO_INVALID_CURSOR(400, "잘못된 커서 값입니다."),
  INDEX_INFO_INVALID_SORT_FIELD(400, "유효하지 않은 필터 값입니다."),

  // indexdata
  INDEX_DATA_INTEGRITY_VIOLATION(400, "중복된 지수 및 날짜 조합입니다."),
  INDEX_BAD_REQUEST(400,"잘못된 요청입니다."),
  INDEX_NOT_FOUND(404,"해당 ID의 지수 정보를 찾을 수 없습니다."),
  INDEX_INTERNAL_SERVER_ERROR(500,"서버 내부 오류가 발생했습니다."),
  
  // syncjobs

  // autosyncconfigs
  AUTOSYNCCONFIGS_NOT_FOUND(404, "auto sync config를 찾지 못했습니다");

  private final int status;
  private final String message;

  ErrorCode(int status, String message) {
    this.status = status;
    this.message = message;
  }

}
