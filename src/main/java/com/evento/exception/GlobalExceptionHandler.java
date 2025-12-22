package com.evento.exception;

import com.evento.model.ApiResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EventoException.class)
    public ResponseEntity<@NonNull ApiResponse<Void>> handleEventoException(EventoException ex) {

        ApiResponse<Void> response = new ApiResponse<>(
                ex.getCode(),
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ApiResponse<Void>> handleGenericException(Exception ex) {

        ApiResponse<Void> response = new ApiResponse<>(
                "9999",
                "Error interno del sistema",
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}