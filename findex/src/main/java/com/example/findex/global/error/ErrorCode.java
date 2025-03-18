package com.example.findex.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
  // 예시
//  USER_NOT_FOUND(400, "유저를 찾지 못했습니다", "잘못된 ID 입니다."),

  // indexinfo
  INDEX_INFO_DUPLICATE_EXCEPTION(400, "지수 정보가 중복입니다.", "지수 분류명, 지수명 조합값은 중복되면 안됩니다."),
  INDEX_INFO_NOT_FOUND(404, "지수 정보를 찾지 못했습니다.", "존재하지 않는 지수 정보입니다."),

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
