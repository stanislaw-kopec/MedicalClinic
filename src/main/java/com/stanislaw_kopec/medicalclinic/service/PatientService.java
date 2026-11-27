package com.stanislaw_kopec.medicalclinic.service;

import com.stanislaw_kopec.medicalclinic.dto.PatientDTO;
import com.stanislaw_kopec.medicalclinic.dto.UpdatePasswordDTO;
import com.stanislaw_kopec.medicalclinic.exception.EmailAlreadyInUseException;
import com.stanislaw_kopec.medicalclinic.exception.PatientNotFoundException;
import com.stanislaw_kopec.medicalclinic.mapper.PatientMapper;
import com.stanislaw_kopec.medicalclinic.model.Patient;
import com.stanislaw_kopec.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientDTO> findAll() {
        return patientRepository.findAll().stream()
                .map(patientMapper::toDTO)
                .toList();
    }

    public PatientDTO get(String email) {
        Patient patient = patientRepository.find(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email " + email + " not found"));
        return patientMapper.toDTO(patient);
    }

    public PatientDTO create(PatientDTO request) {
        Patient patient = patientMapper.toPatient(request);
        if (patientRepository.find(patient.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email " + patient.getEmail() + " is already in use");
        }
        return patientMapper.toDTO(patientRepository.create(patient));
    }

    public void delete(String email) {
        if (!patientRepository.remove(email)) {
            throw new PatientNotFoundException("Patient with email " + email + " not found");
        }
    }

    public PatientDTO edit(PatientDTO request, String email) {
        if (patientRepository.find(email).isEmpty()) {
            throw new PatientNotFoundException("Patient with email " + email + " not found");
        }
        String newEmail = request.email();
        if (!newEmail.equals(email) && patientRepository.find(newEmail).isPresent()) {
            throw new EmailAlreadyInUseException("Email " + newEmail + " is already in use");
        }
        Patient updatedPatient = patientRepository.edit(patientMapper.toPatient(request), email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email " + email + " not found"));
        return patientMapper.toDTO(updatedPatient);
    }

    public void updatePassword(String email, UpdatePasswordDTO request) {
        if (!patientRepository.updatePassword(email, request.password())) {
            throw new PatientNotFoundException("Patient with email " + email + " not found");
        }
    }

}
