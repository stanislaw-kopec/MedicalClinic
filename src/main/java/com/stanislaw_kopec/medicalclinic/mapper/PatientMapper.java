package com.stanislaw_kopec.medicalclinic.mapper;

import com.stanislaw_kopec.medicalclinic.dto.PatientDTO;
import com.stanislaw_kopec.medicalclinic.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PatientMapper {

    @Mapping(target = "password", ignore = true)
    PatientDTO toDTO(Patient patient);

    Patient toPatient(PatientDTO dto);
}
