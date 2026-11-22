package com.stanislaw_kopec.medicalclinic.service;

import com.stanislaw_kopec.medicalclinic.dto.PatientDTO;
import com.stanislaw_kopec.medicalclinic.dto.UpdatePasswordDTO;
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

    public List<PatientDTO> findAll() {
        return patientRepository.findAll().stream()
                .map(this::mapPatientToPatientDTO)
                .toList();
    }

    public PatientDTO get(String email) {
        Patient patient = patientRepository.find(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email " + email + " not found"));
        return mapPatientToPatientDTO(patient);
    }

    public PatientDTO create(PatientDTO request) {
        Patient patient = mapPatientDTOToPatient(request);
        if (patientRepository.find(patient.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email " + patient.getEmail() + " is already in use");
        }
        return mapPatientToPatientDTO(patientRepository.create(patient));
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
        Patient updatedPatient = patientRepository.edit(mapPatientDTOToPatient(request), email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email " + email + " not found"));
        return mapPatientToPatientDTO(updatedPatient);
    }

    public void updatePassword(String email, UpdatePasswordDTO request) {
        if (!patientRepository.updatePassword(email, request.password())) {
            throw new PatientNotFoundException("Patient with email " + email + " not found");
        }
    }

    private PatientDTO mapPatientToPatientDTO(Patient patient) {
        return new PatientDTO(
                patient.getEmail(),
                null,
                patient.getIdCardNo(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getPhoneNumber(),
                patient.getBirthday());
    }

    private Patient mapPatientDTOToPatient(PatientDTO request) {
        Patient patient = new Patient();
        patient.setEmail(request.email());
        patient.setPassword(request.password());
        patient.setIdCardNo(request.idCardNo());
        patient.setFirstName(request.firstName());
        patient.setLastName(request.lastName());
        patient.setPhoneNumber(request.phoneNumber());
        patient.setBirthday(request.birthday());
        return patient;
    }
}
