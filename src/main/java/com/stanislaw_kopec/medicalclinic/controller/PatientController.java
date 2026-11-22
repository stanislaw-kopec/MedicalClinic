package com.stanislaw_kopec.medicalclinic.controller;

import com.stanislaw_kopec.medicalclinic.dto.PatientDTO;
import com.stanislaw_kopec.medicalclinic.dto.UpdatePasswordDTO;
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
    public List<PatientDTO> getAll() {
        return patientService.findAll();
    }

    @GetMapping("/{email}")
    public PatientDTO get(@PathVariable String email) {
        return patientService.get(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDTO create(@Valid @RequestBody PatientDTO request) {
        return patientService.create(request);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String email) {
        patientService.delete(email);
    }

    @PutMapping("/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDTO update(
            @PathVariable String email,
            @Valid @RequestBody PatientDTO request) {
        return patientService.edit(request, email);
    }

    @PatchMapping("/{email}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(
            @PathVariable String email,
            @Valid @RequestBody UpdatePasswordDTO request) {
        patientService.updatePassword(email, request);
    }
}
