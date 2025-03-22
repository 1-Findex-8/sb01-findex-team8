package com.example.findex.common.error.exception.indexdata;

import com.example.findex.common.error.ErrorCode;
import com.example.findex.common.error.exception.BusinessException;

public class IndexDataIntegrityViolationException extends BusinessException {

  public IndexDataIntegrityViolationException(){
    super(ErrorCode.INDEX_DATA_INTEGRITY_VIOLATION);
  }

  public IndexDataIntegrityViolationException(String details) {
    super(ErrorCode.INDEX_DATA_INTEGRITY_VIOLATION, details);
  }
}
