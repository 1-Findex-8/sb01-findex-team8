package com.example.findex.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
  // 예시
//  USER_NOT_FOUND(400, "유저를 찾지 못했습니다", "잘못된 ID 입니다."),

  // indexinfo

  // indexdata

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
