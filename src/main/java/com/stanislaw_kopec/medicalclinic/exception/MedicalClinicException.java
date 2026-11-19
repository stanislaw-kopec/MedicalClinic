package com.stanislaw_kopec.medicalclinic.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class MedicalClinicException extends RuntimeException{

    private final HttpStatus httpStatus;

    protected MedicalClinicException( String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
