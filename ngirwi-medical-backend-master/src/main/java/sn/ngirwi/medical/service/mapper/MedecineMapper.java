package sn.ngirwi.medical.service.mapper;

import org.mapstruct.*;
import sn.ngirwi.medical.domain.Medecine;
import sn.ngirwi.medical.domain.Prescription;
import sn.ngirwi.medical.service.dto.MedecineDTO;
import sn.ngirwi.medical.service.dto.PrescriptionDTO;

/**
 * Mapper for the entity {@link Medecine} and its DTO {@link MedecineDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedecineMapper extends EntityMapper<MedecineDTO, Medecine> {
    @Mapping(target = "ordonance", source = "ordonance", qualifiedByName = "prescriptionId")
    MedecineDTO toDto(Medecine s);

    @Named("prescriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PrescriptionDTO toDtoPrescriptionId(Prescription prescription);
}
