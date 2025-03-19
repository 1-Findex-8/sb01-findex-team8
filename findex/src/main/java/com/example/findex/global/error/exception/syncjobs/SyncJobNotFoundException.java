package com.example.findex.global.error.exception.syncjobs;

import com.example.findex.global.error.ErrorCode;
import com.example.findex.global.error.exception.BusinessException;

public class SyncJobNotFoundException extends BusinessException {

  public SyncJobNotFoundException(String details) {
    super(ErrorCode.SYNCJOB_NOT_FOUND, details);
  }
}
