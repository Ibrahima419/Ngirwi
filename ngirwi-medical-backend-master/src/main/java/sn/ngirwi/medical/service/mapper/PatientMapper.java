package sn.ngirwi.medical.service.mapper;

import org.mapstruct.*;
import sn.ngirwi.medical.domain.Patient;
import sn.ngirwi.medical.service.dto.PatientDTO;

/**
 * Mapper for the entity {@link Patient} and its DTO {@link PatientDTO}.
 */
@Mapper(componentModel = "spring")
public interface PatientMapper extends EntityMapper<PatientDTO, Patient> {}
