package com.optisense.common.exception;

import com.optisense.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
            false,
            ex.getMessage(),
            null,
            LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}