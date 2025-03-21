package com.example.findex.common.error.exception.indexdata;

import com.example.findex.common.error.ErrorCode;
import com.example.findex.common.error.exception.BusinessException;

public class IndexDataInternalServerErrorException extends BusinessException {
  public IndexDataInternalServerErrorException(){
    super(ErrorCode.INDEX_DATA_INTERNAL_SERVER_ERROR);
  }

  public IndexDataInternalServerErrorException(String details) {
    super(ErrorCode.INDEX_DATA_INTERNAL_SERVER_ERROR,details);
  }
}
