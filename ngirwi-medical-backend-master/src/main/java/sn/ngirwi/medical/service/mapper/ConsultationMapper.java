package sn.ngirwi.medical.service.mapper;

import org.mapstruct.*;
import sn.ngirwi.medical.domain.Consultation;
import sn.ngirwi.medical.domain.Patient;
import sn.ngirwi.medical.service.dto.ConsultationDTO;
import sn.ngirwi.medical.service.dto.PatientDTO;

/**
 * Mapper for the entity {@link Consultation} and its DTO {@link ConsultationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConsultationMapper extends EntityMapper<ConsultationDTO, Consultation> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientLastName")
    ConsultationDTO toDto(Consultation s);

    @Named("patientLastName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "lastName", source = "lastName")
    PatientDTO toDtoPatientLastName(Patient patient);
}
