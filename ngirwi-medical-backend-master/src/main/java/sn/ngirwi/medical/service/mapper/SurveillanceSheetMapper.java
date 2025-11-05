package sn.ngirwi.medical.service.mapper;

import org.mapstruct.*;
import sn.ngirwi.medical.domain.*;
import sn.ngirwi.medical.service.dto.SurveillanceSheetDTO;

/**
 * Mapper for the entity {@link SurveillanceSheet} and its DTO {@link SurveillanceSheetDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedicationEntryMapper.class, ActEntryMapper.class })
public interface SurveillanceSheetMapper extends EntityMapper<SurveillanceSheetDTO, SurveillanceSheet> {
    @Mapping(source = "hospitalisation.id", target = "hospitalisationId")
    @Mapping(source = "medications", target = "medications")
    @Mapping(source = "acts", target = "acts")
    SurveillanceSheetDTO toDto(SurveillanceSheet entity);

    @Mapping(source = "hospitalisationId", target = "hospitalisation")
    @Mapping(source = "medications", target = "medications")
    @Mapping(source = "acts", target = "acts")
    SurveillanceSheet toEntity(SurveillanceSheetDTO dto);

    default Hospitalisation fromHospitalisationId(Long id) {
        if (id == null) {
            return null;
        }
        Hospitalisation h = new Hospitalisation();
        h.setId(id);
        return h;
    }
}
