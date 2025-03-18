package com.example.findex.global.error.exception.indexdata;

import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;

public class IndexDataInternalServerErrorException extends BusinessException {
  public IndexDataInternalServerErrorException(){
    super(ErrorCode.INDEX_INTERNAL_SERVER_ERROR);
  }

  public IndexDataInternalServerErrorException(String message) {
    super(ErrorCode.INDEX_INTERNAL_SERVER_ERROR,message);
  }
}
