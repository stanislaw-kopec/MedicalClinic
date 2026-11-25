package com.stanislaw_kopec.medicalclinic.service;

import com.stanislaw_kopec.medicalclinic.dto.PatientDTO;
import com.stanislaw_kopec.medicalclinic.mapper.PatientMapper;
import com.stanislaw_kopec.medicalclinic.model.Patient;
import com.stanislaw_kopec.medicalclinic.repository.PatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class PatientServiceTest {

    PatientMapper patientMapper;
    PatientRepository patientRepository;
    PatientService patientService;

    @BeforeEach
    public void setup() {
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
        this.patientService = new PatientService(patientRepository, patientMapper);
    }

    @Test
    void get_DataCorrect_PatientReturned() {
        //given
        Patient patient = new Patient("jan@wp.pl", "Password123", "CCS343434", "Jan","Kowalski", "111222333", LocalDate.of(2000, 4, 21));
        when(patientRepository.find("jan@wp.pl")).thenReturn(Optional.of(patient));

        //when
        PatientDTO result = patientService.get("jan@wp.pl");

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals("jan@wp.pl", result.email()),
                () -> Assertions.assertEquals("CCS343434", result.idCardNo()),
                () -> Assertions.assertEquals("Jan", result.firstName()),
                () -> Assertions.assertEquals("Kowalski", result.lastName()),
                () -> Assertions.assertEquals("111222333", result.phoneNumber()),
                () -> Assertions.assertEquals(LocalDate.of(2000, 4, 21), result.birthday())
        );
    }

    @Test
    void findAll_DataCorrect_PatientsReturned() {
        //given
        List<Patient> patients = List.of(
            new Patient("anna@gmail.com", "Password123", "WEW111222", "Anna", "Nowak", "333222111", LocalDate.of(2000, 4, 1)),
            new Patient("jan@gmail.com", "Password3434", "SDA334343", "Jan", "Kowalski", "222333777", LocalDate.of(1992, 3, 13)));
        when(patientRepository.findAll()).thenReturn(patients);

        //when
        List<PatientDTO> result = patientService.findAll();

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.size()),
                () -> Assertions.assertEquals("anna@gmail.com", result.getFirst().email()),
                () -> Assertions.assertEquals("WEW111222", result.getFirst().idCardNo()),
                () -> Assertions.assertEquals("Anna", result.getFirst().firstName()),
                () -> Assertions.assertEquals("Nowak", result.getFirst().lastName()),
                () -> Assertions.assertEquals("333222111", result.getFirst().phoneNumber()),
                () -> Assertions.assertEquals(LocalDate.of(2000,4,1), result.getFirst().birthday()),
                () -> Assertions.assertEquals("jan@gmail.com", result.get(1).email()),
                () -> Assertions.assertEquals("SDA334343", result.get(1).idCardNo()),
                () -> Assertions.assertEquals("Jan", result.get(1).firstName()),
                () -> Assertions.assertEquals("Kowalski", result.get(1).lastName()),
                () -> Assertions.assertEquals("222333777", result.get(1).phoneNumber()),
                () -> Assertions.assertEquals(LocalDate.of(1992, 3, 13), result.get(1).birthday())
        );
    }

    @Test
    void create_DataCorrect_PatientCreated() {
        //given
        PatientDTO request = new PatientDTO("jan@wp.pl", "Password123", "CCS343434", "Jan", "Kowalski", "111222333", LocalDate.of(2000, 4, 21));
        Patient patient = new Patient("jan@wp.pl", "Password123", "CCS343434", "Jan","Kowalski", "111222333", LocalDate.of(2000, 4, 21));
        when(patientRepository.create(patient)).thenReturn(patient);

        //when
        PatientDTO result = patientService.create(request);

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals("jan@wp.pl", result.email()),
                () -> Assertions.assertEquals("CCS343434", result.idCardNo()),
                () -> Assertions.assertEquals("Jan", result.firstName()),
                () -> Assertions.assertEquals("Kowalski", result.lastName()),
                () -> Assertions.assertEquals("111222333", result.phoneNumber()),
                () -> Assertions.assertEquals(LocalDate.of(2000, 4, 21), result.birthday()),
                () -> Assertions.assertNull(result.password())
        );
        Mockito.verify(patientRepository).create(patient);
    }

    @Test
    void edit_RequestDataCorrectAndPatientWithEmailFound_PatientUpdated() {
        //given
        Patient patient = new Patient("marta@wp.pl", "MySecret123", "CDE888434", "Marta", "Nowak", "405405405", LocalDate.of(2001, 2, 16));
        Patient updatedPatient = new Patient("marta@wp.pl", "MySecret123", "CDE888434", "Marta", "Nowak-Kowalska", "405405405", LocalDate.of(2001, 2, 16));
        PatientDTO request = new PatientDTO("marta@wp.pl", "MySecret123", "CDE888434", "Marta", "Nowak-Kowalska", "405405405", LocalDate.of(2001, 2, 16));
        String email = "marta@wp.pl";
        when(patientRepository.find(email)).thenReturn(Optional.of(patient));
        when(patientRepository.edit(updatedPatient, email)).thenReturn(Optional.of(updatedPatient));

        //when
        PatientDTO result = patientService.edit(request, email);

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals("marta@wp.pl", result.email()),
                () -> Assertions.assertEquals("CDE888434", result.idCardNo()),
                () -> Assertions.assertEquals("Marta", result.firstName()),
                () -> Assertions.assertEquals("Nowak-Kowalska", result.lastName()),
                () -> Assertions.assertEquals("405405405", result.phoneNumber()),
                () -> Assertions.assertEquals(LocalDate.of(2001, 2, 16), result.birthday()),
                () -> Assertions.assertNull(result.password())
        );
        Mockito.verify(patientRepository).edit(updatedPatient, email);
    }
}
