package com.stanislaw_kopec.medicalclinic.repository;

import com.stanislaw_kopec.medicalclinic.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepository {
    private final List<Patient> patients = new ArrayList<>();

    public List<Patient> findAll() {
        return new ArrayList<>(patients);
    }

    public Optional<Patient> findByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }

    public Patient addPatient(Patient patient) {
        patients.add(patient);
        return patient;
    }

    public boolean removePatientByEmail(String email) {
        return findByEmail(email)
                .map(patients::remove)
                .orElse(false);
    }

    public boolean editPatientByEmail(Patient updatedPatient, String email) {
        Optional<Patient> existingPatient = findByEmail(email);

        if (existingPatient.isPresent()) {
            int index = patients.indexOf(existingPatient.get());
            patients.set(index, updatedPatient);
            return true;
        }
        return false;
    }
}
