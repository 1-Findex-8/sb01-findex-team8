package com.example.findex.global.error.exception.indexdata;

import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;

public class IndexDataIntegrityViolationException extends BusinessException {

  public IndexDataIntegrityViolationException(){
    super(ErrorCode.INDEX_DATA_INTEGRITY_VIOLATION);
  }

  public IndexDataIntegrityViolationException(String details) {
    super(ErrorCode.INDEX_DATA_INTEGRITY_VIOLATION, details);
  }
}
