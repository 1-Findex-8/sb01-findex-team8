package com.example.findex.global.error.exception.indexinfo;

import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;

public class IndexInfoInvalidCursor extends BusinessException {
  public IndexInfoInvalidCursor() { super(ErrorCode.INDEX_INFO_INVALID_CURSOR); }
  public IndexInfoInvalidCursor(String message) {
    super(ErrorCode.INDEX_INFO_INVALID_CURSOR, message);
  }

}
