package com.example.findex.global.error.exception.indexinfo;

import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;

public class IndexInfoNotFound extends BusinessException {

  public IndexInfoNotFound() {
    super(ErrorCode.INDEX_INFO_NOT_FOUND);
  }
  public IndexInfoNotFound(String message) {
    super(ErrorCode.INDEX_INFO_NOT_FOUND, message);
  }
}
