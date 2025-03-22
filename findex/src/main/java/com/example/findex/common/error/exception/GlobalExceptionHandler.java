package com.example.findex.common.error.exception;

import com.example.findex.common.error.ErrorResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
    ErrorResponse errorResponse = new ErrorResponse(
        LocalDateTime.now(),
        exception.getErrorCode().getStatus(),
        exception.getErrorCode().getMessage(),
        exception.getDetails()
    );
    return ResponseEntity.status(HttpStatusCode.valueOf(exception.getErrorCode().getStatus()))
        .body(errorResponse);
  }
}
