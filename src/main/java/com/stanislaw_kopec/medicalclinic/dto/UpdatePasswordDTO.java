package com.stanislaw_kopec.medicalclinic.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordDTO(@NotBlank String password) {
}
