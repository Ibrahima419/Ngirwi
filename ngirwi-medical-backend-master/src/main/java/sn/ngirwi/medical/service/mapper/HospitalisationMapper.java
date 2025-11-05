package sn.ngirwi.medical.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import sn.ngirwi.medical.domain.*;
import sn.ngirwi.medical.service.dto.HospitalisationDTO;

/**
 * Mapper for the entity {@link Hospitalisation} and its DTO {@link HospitalisationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HospitalisationMapper {
    // Entity -> DTO

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "dossierMedical.id", target = "dossierMedicalId")
    @Mapping(target = "surveillanceSheetIds", expression = "java(mapSurveillanceSheetIds(hospitalisation.getSurveillanceSheets()))")
    @Mapping(target = "dailyRate", source = "dailyRate")
    @Mapping(target = "comfortFees", source = "comfortFees")
    @Mapping(target = "feeOverrun", source = "feeOverrun")
    @Mapping(target = "insuranceCoveragePercent", source = "insuranceCoveragePercent")
    @Mapping(target = "totalAmount", source = "totalAmount")
    HospitalisationDTO toDto(Hospitalisation hospitalisation);

    // DTO -> Entity

    @Mapping(source = "patientId", target = "patient")
    @Mapping(source = "dossierMedicalId", target = "dossierMedical")
    @Mapping(target = "surveillanceSheets", ignore = true)
    @Mapping(target = "dailyRate", source = "dailyRate")
    @Mapping(target = "comfortFees", source = "comfortFees")
    @Mapping(target = "feeOverrun", source = "feeOverrun")
    @Mapping(target = "insuranceCoveragePercent", source = "insuranceCoveragePercent")
    @Mapping(target = "totalAmount", source = "totalAmount")
    Hospitalisation toEntity(HospitalisationDTO dto);

    // Helper Methods

    default Set<Long> mapSurveillanceSheetIds(Set<SurveillanceSheet> sheets) {
        return sheets != null ? sheets.stream().map(SurveillanceSheet::getId).collect(Collectors.toSet()) : null;
    }

    default Patient fromPatientId(Long id) {
        if (id == null) {
            return null;
        }
        Patient p = new Patient();
        p.setId(id);
        return p;
    }

    default DossierMedical fromDossierMedicalId(Long id) {
        if (id == null) {
            return null;
        }
        DossierMedical d = new DossierMedical();
        d.setId(id);
        return d;
    }
}
