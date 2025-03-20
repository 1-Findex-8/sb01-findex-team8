package com.example.findex.global.error.exception.indexInfo;

import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;

public class IndexInfoNotFoundException extends BusinessException {

  public IndexInfoNotFoundException() {
    super(ErrorCode.INDEX_INFO_NOT_FOUND);
  }
}
