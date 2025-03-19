package com.example.findex.global.error.exception.indexinfo;

import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;

public class IndexInfoDuplicate extends BusinessException {
  public IndexInfoDuplicate() { super(ErrorCode.INDEX_INFO_DUPLICATE_EXCEPTION); }
  public IndexInfoDuplicate(String message) {
    super(ErrorCode.INDEX_INFO_DUPLICATE_EXCEPTION, message);
  }

}
