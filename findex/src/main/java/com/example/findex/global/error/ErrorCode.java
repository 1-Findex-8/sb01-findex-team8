package com.example.findex.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
  // 예시
  USER_NOT_FOUND(400, "유저를 찾지 못했습니다", "잘못된 ID 입니다."),

  // indexinfo

  // indexdata

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
