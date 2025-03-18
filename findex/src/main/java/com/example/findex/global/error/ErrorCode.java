package com.example.findex.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
  // 예시
  USER_NOT_FOUND(400, "유저를 찾지 못했습니다", "잘못된 ID 입니다."),

  // indexinfo

  // indexdata
  INDEX_DATA_INTEGRITY_VIOLATION(400, "중복된 지수 및 날짜 조합입니다.",""),
  INDEX_BAD_REQUEST(400,"잘못된 요청입니다.",""),
  INDEX_NOT_FOUND(404,"해당 ID의 지수 정보를 찾을 수 없습니다.",""),
  INDEX_INTERNAL_SERVER_ERROR(500,"서버 내부 오류가 발생했습니다.",""),
  // syncjobs

  // autosyncconfigs
  AUTOSYNCCONFIGS_NOT_FOUND(404, "auto sync config를 찾지 못했습니다", "존재하지 않는 id입니다.");

  private final int status;
  private final String message;
  private final String details;

  ErrorCode(int status, String message, String details) {
    this.status = status;
    this.message = message;
    this.details = details;
  }
}
