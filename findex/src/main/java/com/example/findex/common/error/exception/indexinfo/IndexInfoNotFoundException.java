package com.example.findex.common.error.exception.indexinfo;

import com.example.findex.common.error.ErrorCode;
import com.example.findex.common.error.exception.BusinessException;

public class IndexInfoNotFoundException extends BusinessException {

  public IndexInfoNotFoundException() {
    super(ErrorCode.INDEX_INFO_NOT_FOUND);
  }
  public IndexInfoNotFoundException(String message) {
    super(ErrorCode.INDEX_INFO_NOT_FOUND, message);
  }
}