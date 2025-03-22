package com.example.findex.common.error.exception.indexinfo;

import com.example.findex.common.error.ErrorCode;
import com.example.findex.common.error.exception.BusinessException;

public class IndexInfoInvalidSortFieldException extends BusinessException {
  public IndexInfoInvalidSortFieldException() { super(ErrorCode.INDEX_INFO_INVALID_SORT_FIELD); }
  public IndexInfoInvalidSortFieldException(String message) {
    super(ErrorCode.INDEX_INFO_INVALID_SORT_FIELD, message);
  }

}
