package sn.ngirwi.medical.service.mapper;

import org.mapstruct.*;
import sn.ngirwi.medical.domain.DossierMedical;
import sn.ngirwi.medical.domain.Patient;
import sn.ngirwi.medical.service.dto.DossierMedicalDTO;
import sn.ngirwi.medical.service.dto.PatientDTO;

/**
 * Mapper for the entity {@link DossierMedical} and its DTO {@link DossierMedicalDTO}.
 */
@Mapper(componentModel = "spring")
public interface DossierMedicalMapper extends EntityMapper<DossierMedicalDTO, DossierMedical> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientId")
    DossierMedicalDTO toDto(DossierMedical s);

    @Named("patientId")
    //@BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientDTO toDtoPatientId(Patient patient);
}
