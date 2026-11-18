package com.stanislaw_kopec.medicalclinic.service;

import com.stanislaw_kopec.medicalclinic.exception.EmailAlreadyInUseException;
import com.stanislaw_kopec.medicalclinic.exception.PatientNotFoundException;
import com.stanislaw_kopec.medicalclinic.model.Patient;
import com.stanislaw_kopec.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Patient get(String email) {
        return patientRepository.find(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }

    public Patient create(Patient patient) {
        if (patientRepository.find(patient.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException(patient.getEmail());
        }
        return patientRepository.create(patient);
    }

    public void delete(String email) {
        if (!patientRepository.remove(email)) {
            throw new PatientNotFoundException(email);
        }
    }

    public Patient edit(Patient patient, String email) {
        if (patientRepository.find(email).isEmpty()) {
            throw new PatientNotFoundException(email);
        }
        String newEmail = patient.getEmail();
        if (!newEmail.equals(email) && patientRepository.find(newEmail).isPresent()) {
            throw new EmailAlreadyInUseException(newEmail);
        }
        return patientRepository.edit(patient, email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }

    public void updatePassword(String email, String password) {
        if (!patientRepository.updatePassword(email, password)) {
            throw new PatientNotFoundException(email);
        }
    }
}
