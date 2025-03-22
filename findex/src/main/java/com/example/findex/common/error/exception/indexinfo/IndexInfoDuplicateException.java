package com.example.findex.common.error.exception.indexinfo;

import com.example.findex.common.error.ErrorCode;
import com.example.findex.common.error.exception.BusinessException;

public class IndexInfoDuplicateException extends BusinessException {
  public IndexInfoDuplicateException() { super(ErrorCode.INDEX_INFO_DUPLICATE_EXCEPTION); }
  public IndexInfoDuplicateException(String message) {
    super(ErrorCode.INDEX_INFO_DUPLICATE_EXCEPTION, message);
  }

}
