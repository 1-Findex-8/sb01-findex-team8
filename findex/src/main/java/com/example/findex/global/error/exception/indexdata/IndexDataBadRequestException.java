package com.example.findex.global.error.exception.indexdata;

import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;

public class IndexDataBadRequestException extends BusinessException {
  public IndexDataBadRequestException(){
    super(ErrorCode.INDEX_BAD_REQUEST);
  }
  public IndexDataBadRequestException(String message) {
    super(ErrorCode.INDEX_BAD_REQUEST,message);
  }
}
