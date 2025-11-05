package sn.ngirwi.medical.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import sn.ngirwi.medical.domain.MiniConsultation;
import sn.ngirwi.medical.domain.SurveillanceSheet;
import sn.ngirwi.medical.service.dto.MiniConsultationDTO;

/**
 * Mapper for the entity {@link MiniConsultation} and its DTO {@link MiniConsultationDTO}.
 *
 * - Maps surveillanceSheet.id <-> surveillanceSheetId
 * - Leaves prescriptionId as-is (scalar)
 */
@Mapper(componentModel = "spring")
public interface MiniConsultationMapper {
    @Mapping(source = "surveillanceSheet.id", target = "surveillanceSheetId")
    MiniConsultationDTO toDto(MiniConsultation entity);

    @Mapping(source = "surveillanceSheetId", target = "surveillanceSheet")
    MiniConsultation toEntity(MiniConsultationDTO dto);

    /**
     * Utility to create a SurveillanceSheet placeholder from its id.
     * MapStruct will use this method when mapping surveillanceSheetId -> surveillanceSheet.
     */
    default SurveillanceSheet surveillanceSheetFromId(Long id) {
        if (id == null) {
            return null;
        }
        SurveillanceSheet s = new SurveillanceSheet();
        s.setId(id);
        return s;
    }

    /**
     * Utility to create a MiniConsultation with only id set.
     * Can be useful in certain mappings or tests.
     */
    default MiniConsultation fromId(Long id) {
        if (id == null) {
            return null;
        }
        MiniConsultation mc = new MiniConsultation();
        mc.setId(id);
        return mc;
    }
}
