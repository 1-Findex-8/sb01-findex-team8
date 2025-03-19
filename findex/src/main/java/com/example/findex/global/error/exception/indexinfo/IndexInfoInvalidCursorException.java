package com.example.findex.global.error.exception.indexinfo;

import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;

public class IndexInfoInvalidCursorException extends BusinessException {
  public IndexInfoInvalidCursorException() { super(ErrorCode.INDEX_INFO_INVALID_CURSOR); }
  public IndexInfoInvalidCursorException(String message) {
    super(ErrorCode.INDEX_INFO_INVALID_CURSOR, message);
  }

}
