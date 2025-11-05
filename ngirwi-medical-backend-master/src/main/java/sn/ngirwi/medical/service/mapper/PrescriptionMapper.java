package sn.ngirwi.medical.service.mapper;

import org.mapstruct.*;
import sn.ngirwi.medical.domain.Consultation;
import sn.ngirwi.medical.domain.Prescription;
import sn.ngirwi.medical.service.dto.ConsultationDTO;
import sn.ngirwi.medical.service.dto.PrescriptionDTO;

/**
 * Mapper for the entity {@link Prescription} and its DTO {@link PrescriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PrescriptionMapper extends EntityMapper<PrescriptionDTO, Prescription> {
    @Mapping(target = "consultation", source = "consultation", qualifiedByName = "consultationId")
    PrescriptionDTO toDto(Prescription s);

    @Named("consultationId")
    //@BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConsultationDTO toDtoConsultationId(Consultation consultation);
}
