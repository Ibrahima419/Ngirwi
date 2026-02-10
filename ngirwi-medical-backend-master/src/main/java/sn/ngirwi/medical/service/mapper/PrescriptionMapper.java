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

    /**
     * Default toEntity maps medecines from PrescriptionForm → Medecine.
     * This causes duplicates when used in updateBis() because Hibernate's merge
     * conflicts with the explicit medicine CRUD in the service.
     *
     * Override: ignore medecines so the service manages them explicitly.
     */
    @Override
    @Mapping(target = "medecines", ignore = true)
    Prescription toEntity(PrescriptionDTO dto);

    @Named("consultationId")
    @Mapping(target = "id", source = "id")
    ConsultationDTO toDtoConsultationId(Consultation consultation);
}
