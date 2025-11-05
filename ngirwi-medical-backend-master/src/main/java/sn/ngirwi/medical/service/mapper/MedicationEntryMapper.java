package sn.ngirwi.medical.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.ngirwi.medical.domain.MedicationEntry;
import sn.ngirwi.medical.service.dto.MedicationEntryDTO;

@Mapper(componentModel = "spring")
public interface MedicationEntryMapper {
    @Mapping(source = "nom", target = "name")
    @Mapping(source = "prixUnitaire", target = "unitPrice")
    @Mapping(source = "quantite", target = "quantity")
    MedicationEntryDTO toDto(MedicationEntry entity);

    @Mapping(source = "name", target = "nom")
    @Mapping(source = "unitPrice", target = "prixUnitaire")
    @Mapping(source = "quantity", target = "quantite")
    MedicationEntry toEntity(MedicationEntryDTO dto);
}


