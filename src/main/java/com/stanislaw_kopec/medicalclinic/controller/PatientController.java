package com.stanislaw_kopec.medicalclinic.controller;

import com.stanislaw_kopec.medicalclinic.model.Patient;
import com.stanislaw_kopec.medicalclinic.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public List<Patient> getAll() {
        return patientService.findAll();
    }

    @GetMapping("/{email}")
    public Patient get(@PathVariable String email) {
        return patientService.get(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient create(@Valid @RequestBody Patient patient) {
        return patientService.create(patient);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String email) {
        patientService.delete(email);
    }

    @PutMapping("/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    public Patient update(
            @PathVariable String email,
            @Valid @RequestBody Patient patient) {
        return patientService.edit(patient, email);
    }

    @PatchMapping("/{email}/{password}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(
            @PathVariable String email,
            @PathVariable String password) {
        patientService.updatePassword(email, password);
    }
}
