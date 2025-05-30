package com.example.findex.common.error.exception.indexdata;

import com.example.findex.common.error.ErrorCode;
import com.example.findex.common.error.exception.BusinessException;

public class IndexDataBadRequestException extends BusinessException {
  public IndexDataBadRequestException(){
    super(ErrorCode.INDEX_DATA_BAD_REQUEST);
  }
  public IndexDataBadRequestException(String details) {
    super(ErrorCode.INDEX_DATA_BAD_REQUEST,details);
  }
}
