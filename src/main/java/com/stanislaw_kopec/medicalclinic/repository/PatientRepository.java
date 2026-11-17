package com.stanislaw_kopec.medicalclinic.repository;

import com.stanislaw_kopec.medicalclinic.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PatientRepository {

    private final List<Patient> patients;

    public List<Patient> findAll() {
        return new ArrayList<>(patients);
    }

    public Optional<Patient> find(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }

    public Patient create(Patient patient) {
        patients.add(patient);
        return patient;
    }

    public boolean remove(String email) {
        return find(email)
                .map(patients::remove)
                .orElse(false);
    }

    public Optional<Patient> edit(Patient updatedPatient, String email) {
        return find(email).map(patient -> {
            patient.setEmail(updatedPatient.getEmail());
            patient.setPassword(updatedPatient.getPassword());
            patient.setIdCardNo(updatedPatient.getIdCardNo());
            patient.setFirstName(updatedPatient.getFirstName());
            patient.setLastName(updatedPatient.getLastName());
            patient.setPhoneNumber(updatedPatient.getPhoneNumber());
            patient.setBirthday(updatedPatient.getBirthday());
            return patient;
        });
    }
}
