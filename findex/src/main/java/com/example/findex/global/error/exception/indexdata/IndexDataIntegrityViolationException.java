package com.example.findex.global.error.exception.indexdata;

import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;

public class IndexDataIntegrityViolationException extends BusinessException {

  public IndexDataIntegrityViolationException(){
    super(ErrorCode.INDEXDATA_INTEGRITY_VIOLATION);
  }

  public IndexDataIntegrityViolationException(String message) {
    super(ErrorCode.INDEXDATA_INTEGRITY_VIOLATION, message);
  }
}
