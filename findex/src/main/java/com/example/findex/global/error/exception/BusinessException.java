package com.example.findex.global.error.exception;

import com.example.findex.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

  private final ErrorCode errorCode;
  private String details = "";

  public BusinessException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  public BusinessException(ErrorCode errorCode, String details) {
    this.errorCode = errorCode;
    this.details = details;
  }
}
