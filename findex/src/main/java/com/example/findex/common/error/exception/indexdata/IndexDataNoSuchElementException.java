package com.example.findex.common.error.exception.indexdata;

import com.example.findex.common.error.ErrorCode;
import com.example.findex.common.error.exception.BusinessException;

public class IndexDataNoSuchElementException extends BusinessException {

  public IndexDataNoSuchElementException() {
    super(ErrorCode.INDEX_DATA_NOT_FOUND);
  }

  public IndexDataNoSuchElementException(String details) {
    super(ErrorCode.INDEX_DATA_NOT_FOUND,details);
  }
}
