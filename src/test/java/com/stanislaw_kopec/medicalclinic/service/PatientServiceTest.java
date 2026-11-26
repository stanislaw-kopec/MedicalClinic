package com.stanislaw_kopec.medicalclinic.service;

import com.stanislaw_kopec.medicalclinic.dto.PatientDTO;
import com.stanislaw_kopec.medicalclinic.dto.UpdatePasswordDTO;
import com.stanislaw_kopec.medicalclinic.exception.EmailAlreadyInUseException;
import com.stanislaw_kopec.medicalclinic.exception.PatientNotFoundException;
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

    @Test
    void edit_NewEmailIsAvailable_PatientUpdated() {
        //given
        String email = "marta@wp.pl";
        String newEmail = "marta1@wp.pl";
        Patient patient = new Patient(email, "MySecret123", "CDE888434", "Marta", "Nowak", "405405405", LocalDate.of(2001, 2, 16));
        PatientDTO request = new PatientDTO(newEmail, "MySecret123", "CDE888434", "Marta", "Nowak-Kowalska", "405405405", LocalDate.of(2001, 2, 16));
        Patient updatedPatient = new Patient(newEmail, "MySecret123", "CDE888434", "Marta", "Nowak-Kowalska", "405405405", LocalDate.of(2001, 2, 16));
        when(patientRepository.find(email)).thenReturn(Optional.of(patient));
        when(patientRepository.find(newEmail)).thenReturn(Optional.empty());
        when(patientRepository.edit(updatedPatient, email)).thenReturn(Optional.of(updatedPatient));

        //when
        PatientDTO result = patientService.edit(request, email);

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(newEmail, result.email()),
                () -> Assertions.assertEquals("CDE888434", result.idCardNo()),
                () -> Assertions.assertEquals("Marta", result.firstName()),
                () -> Assertions.assertEquals("Nowak-Kowalska", result.lastName()),
                () -> Assertions.assertEquals("405405405", result.phoneNumber()),
                () -> Assertions.assertEquals(LocalDate.of(2001, 2, 16), result.birthday()),
                () -> Assertions.assertNull(result.password())
        );
        Mockito.verify(patientRepository).find(email);
        Mockito.verify(patientRepository).find(newEmail);
        Mockito.verify(patientRepository).edit(updatedPatient, email);
    }

    @Test
    void edit_PatientFoundButRepositoryEditReturnsEmpty_PatientNotFoundExceptionThrown() {
        //given
        String email = "marta@wp.pl";
        Patient patient = new Patient(email, "MySecret123", "CDE888434", "Marta", "Nowak", "405405405", LocalDate.of(2001, 2, 16));
        PatientDTO request = new PatientDTO(email, "MySecret123", "CDE888434", "Marta", "Nowak-Kowalska", "405405405", LocalDate.of(2001, 2, 16));
        Patient updatedPatient = new Patient(email, "MySecret123", "CDE888434", "Marta", "Nowak-Kowalska", "405405405", LocalDate.of(2001, 2, 16));
        when(patientRepository.find(email)).thenReturn(Optional.of(patient));
        when(patientRepository.edit(updatedPatient, email)).thenReturn(Optional.empty());

        //when and then
        PatientNotFoundException exception = Assertions.assertThrows(
                PatientNotFoundException.class, () -> patientService.edit(request, email));
        Assertions.assertEquals("Patient with email " + email + " not found", exception.getMessage());
        Mockito.verify(patientRepository).find(email);
        Mockito.verify(patientRepository).edit(updatedPatient, email);
    }

    @Test
    void get_DataIncorrect_PatientNotFoudExceptionThrow() {
        //given
        String email = "jan@gmail.com";
        when(patientRepository.find(email)).thenReturn(Optional.empty());

        //when and then
        PatientNotFoundException exception = Assertions.assertThrows(PatientNotFoundException.class, () -> patientService.get(email));
        Assertions.assertEquals("Patient with email " + email + " not found", exception.getMessage());
        Mockito.verify(patientRepository).find(email);
    }

    @Test
    void create_ProvidedEmailIsInUse_EmailAlreadyInUseException() {
        //given
        String email = "anna123@wp.pl";
        Patient patient = new Patient(email, "Password1234", "CCC123123", "Anna", "Kowalska", "123500300", LocalDate.of(2000, 3, 3));
        PatientDTO patientDTO = new PatientDTO(email, "Password1234", "CCC123123", "Anna", "Kowalska", "123500300", LocalDate.of(2000, 3, 3));
        when(patientRepository.find(email)).thenReturn(Optional.of(patient));

        //when and then
        EmailAlreadyInUseException exception = Assertions.assertThrows(EmailAlreadyInUseException.class, () -> patientService.create(patientDTO));
        Assertions.assertEquals("Email " + email + " is already in use", exception.getMessage());
        Mockito.verify(patientRepository).find(email);
    }

    @Test
    void updatePassword_PatientWithEmailFound_PasswordUpdated() {
        //given
        String email = "anna123@wp.pl";
        UpdatePasswordDTO request = new UpdatePasswordDTO("NewSecret123");
        when(patientRepository.updatePassword(email, request.password())).thenReturn(true);

        //when
        patientService.updatePassword(email, request);

        //then
        Mockito.verify(patientRepository).updatePassword(email, request.password());
    }

    @Test
    void updatePassword_PatientWithEmailNotFound_PatientNotFoundExceptionThrown() {
        //given
        String email = "anna123@wp.pl";
        UpdatePasswordDTO request = new UpdatePasswordDTO("NewSecret123");
        when(patientRepository.updatePassword(email, request.password())).thenReturn(false);

        //when and then
        PatientNotFoundException exception = Assertions.assertThrows(PatientNotFoundException.class, () -> patientService.updatePassword(email, request));
        Assertions.assertEquals("Patient with email " + email + " not found", exception.getMessage());
        Mockito.verify(patientRepository).updatePassword(email, request.password());
    }

    @Test
    void delete_PatientWithEmailFound_PatientSuccessfullyDeleted() {
        //given
        String email = "anna123@wp.pl";
        when(patientRepository.remove(email)).thenReturn(true);

        //when
        patientService.delete(email);

        //then
        Mockito.verify(patientRepository).remove(email);
    }

    @Test
    void delete_PatientWithEmailNotFound_EmailNotFoundException() {
        //given
        String email = "anna123@wp.pl";
        when(patientRepository.remove(email)).thenReturn(false);

        //when and then
        PatientNotFoundException exception = Assertions.assertThrows(PatientNotFoundException.class, () -> patientService.delete(email));
        Assertions.assertEquals("Patient with email " + email + " not found", exception.getMessage());
        Mockito.verify(patientRepository).remove(email);
    }

    @Test
    void edit_PatientWithEmailNotFound_EmailNotFoundException() {
        //given
        String email = "marek999@gmail.com";
        PatientDTO request = new PatientDTO("marek999@gmail.com", "MySecret123", "CDE888434", "Marek", "Nawrocki", "405405405", LocalDate.of(2001, 2, 16));
        when(patientRepository.find(email)).thenReturn(Optional.empty());

        //when and then
        PatientNotFoundException exception = Assertions.assertThrows(PatientNotFoundException.class, () -> patientService.edit(request, email));
        Assertions.assertEquals("Patient with email " + email + " not found", exception.getMessage());
        Mockito.verify(patientRepository).find(email);
    }

    @Test
    void edit_ProvidedEmailIsNotEqualsToRequestEmailAndRequestEmailIsInUse_EmailAlreadyInUseException() {
        //given
        String email = "marek999@gmail.com";
        PatientDTO request = new PatientDTO("marek777@gmail.com" , "MySecret123", "CDE888434", "Marek", "Nawrocki", "405405405", LocalDate.of(2001, 2, 16));
        Patient patient = new Patient(email , "MyPassword123", "NNN888434", "Marek", "Nawrocki", "405405405", LocalDate.of(2001, 2, 16));
        Patient otherPatient = new Patient("marek777@gmail.com", "OtherPassword123", "ABC123456", "Jan", "Kowalski", "123456789", LocalDate.of(1990, 1, 1));
        when(patientRepository.find(email)).thenReturn(Optional.of(patient));
        when(patientRepository.find(request.email())).thenReturn(Optional.of(otherPatient));

        //when and then
        EmailAlreadyInUseException exception = Assertions.assertThrowsExactly(EmailAlreadyInUseException.class, () -> patientService.edit(request, email));
        Assertions.assertEquals("Email " + request.email() + " is already in use", exception.getMessage());
        Mockito.verify(patientRepository).find(email);
        Mockito.verify(patientRepository).find(request.email());
    }
}
