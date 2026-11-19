package com.stanislaw_kopec.medicalclinic.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MedicalClinicException.class)
    public ResponseEntity<String> handleMedicalClinicException(MedicalClinicException exception) {
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(exception.getMessage());
    }
}
