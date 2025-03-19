package com.example.findex.global.error.exception.indexinfo;

import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;

public class IndexInfoDuplicateException extends BusinessException {
  public IndexInfoDuplicateException() { super(ErrorCode.INDEX_INFO_DUPLICATE_EXCEPTION); }
  public IndexInfoDuplicateException(String message) {
    super(ErrorCode.INDEX_INFO_DUPLICATE_EXCEPTION, message);
  }

}
