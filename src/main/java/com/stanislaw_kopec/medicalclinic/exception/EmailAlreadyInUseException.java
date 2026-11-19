package com.stanislaw_kopec.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyInUseException extends MedicalClinicException {

    public EmailAlreadyInUseException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
