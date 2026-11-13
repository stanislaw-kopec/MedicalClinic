package com.stanislaw_kopec.medicalclinic.service;

import com.stanislaw_kopec.medicalclinic.exception.EmailAlreadyInUseException;
import com.stanislaw_kopec.medicalclinic.model.Patient;
import com.stanislaw_kopec.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    public Patient addPatient(Patient patient) {
        if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException(patient.getEmail());
        }

        return patientRepository.addPatient(patient);
    }

    public boolean deletePatientByEmail(String email) {
        return patientRepository.removePatientByEmail(email);
    }

    public boolean editPatientByEmail(Patient patient, String email) {
        if (patientRepository.findByEmail(email).isEmpty()) {
            return false;
        }

        String newEmail = patient.getEmail();
        if (!newEmail.equals(email) && patientRepository.findByEmail(newEmail).isPresent()) {
            throw new EmailAlreadyInUseException(newEmail);
        }

        return patientRepository.editPatientByEmail(patient, email);
    }

}
