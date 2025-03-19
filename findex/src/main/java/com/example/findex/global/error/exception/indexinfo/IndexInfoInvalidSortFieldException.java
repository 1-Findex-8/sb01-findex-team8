package com.example.findex.global.error.exception.indexinfo;

import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;

public class IndexInfoInvalidSortField extends BusinessException {
  public IndexInfoInvalidSortField() { super(ErrorCode.INDEX_INFO_INVALID_SORT_FIELD); }
  public IndexInfoInvalidSortField(String message) {
    super(ErrorCode.INDEX_INFO_INVALID_SORT_FIELD, message);
  }

}
