package com.example.findex.global.error.exception.indexinfo;

import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;

public class IndexInfoNotFoundException extends BusinessException {

  public IndexInfoNotFoundException() {
    super(ErrorCode.INDEXINFO_NOT_FOUND);
  }
}
