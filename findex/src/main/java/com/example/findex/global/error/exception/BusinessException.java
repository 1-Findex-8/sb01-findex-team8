package com.example.findex.global.error.exception;

import com.example.findex.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BusinessException extends RuntimeException {

  private final ErrorCode errorCode;
  private String details = ""; // details는 필수적이지 않다고 생각해서 ""로 초기화

  public BusinessException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  // details가 포함된 생성자로 변경 가능
  public BusinessException(ErrorCode errorCode, String details) {
    this.errorCode = errorCode;
    this.details = details;
  }
}
