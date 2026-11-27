package com.stanislaw_kopec.medicalclinic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record PatientDTO(
        @NotBlank @Email String email,
        @NotBlank @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String password,
        @NotBlank String idCardNo,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String phoneNumber,
        @NotNull @PastOrPresent LocalDate birthday) {
}
