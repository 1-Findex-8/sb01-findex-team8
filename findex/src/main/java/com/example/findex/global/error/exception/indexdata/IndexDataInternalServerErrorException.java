package com.example.findex.global.error.exception.indexdata;

import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;

public class IndexDataInternalServerErrorException extends BusinessException {
  public IndexDataInternalServerErrorException(){
    super(ErrorCode.INDEXDATA_INTERNAL_SERVER_ERROR);
  }

  public IndexDataInternalServerErrorException(String details) {
    super(ErrorCode.INDEXDATA_INTERNAL_SERVER_ERROR,details);
  }
}
