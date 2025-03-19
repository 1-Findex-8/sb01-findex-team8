package com.example.findex.global.error.exception.indexdata;

import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;

public class IndexDataNoSuchElementException extends BusinessException {

  public IndexDataNoSuchElementException() {
    super(ErrorCode.INDEXDATA_NOT_FOUND);
  }

  public IndexDataNoSuchElementException(String message) {
    super(ErrorCode.INDEXDATA_NOT_FOUND,message);
  }
}
