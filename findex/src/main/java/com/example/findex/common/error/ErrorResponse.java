package com.example.findex.common.error;

import java.time.LocalDateTime;

public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String message,
    String details
) {

}
