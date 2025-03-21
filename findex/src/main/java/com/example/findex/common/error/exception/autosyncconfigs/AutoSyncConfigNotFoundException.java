package com.example.findex.common.error.exception.autosyncconfigs;

import com.example.findex.common.error.ErrorCode;
import com.example.findex.common.error.exception.BusinessException;

public class AutoSyncConfigNotFoundException extends BusinessException {

  public AutoSyncConfigNotFoundException() {
    super(ErrorCode.AUTOSYNCCONFIGS_NOT_FOUND);
  }

  public AutoSyncConfigNotFoundException(String details) {
    super(ErrorCode.AUTOSYNCCONFIGS_NOT_FOUND, details);
  }
}
