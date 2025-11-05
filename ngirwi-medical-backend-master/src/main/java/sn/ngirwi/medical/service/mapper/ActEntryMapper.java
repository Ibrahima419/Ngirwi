package sn.ngirwi.medical.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.ngirwi.medical.domain.ActEntry;
import sn.ngirwi.medical.service.dto.ActEntryDTO;

@Mapper(componentModel = "spring")
public interface ActEntryMapper {
    @Mapping(source = "nom", target = "name")
    @Mapping(source = "prixUnitaire", target = "unitPrice")
    @Mapping(source = "quantite", target = "quantity")
    ActEntryDTO toDto(ActEntry entity);

    @Mapping(source = "name", target = "nom")
    @Mapping(source = "unitPrice", target = "prixUnitaire")
    @Mapping(source = "quantity", target = "quantite")
    ActEntry toEntity(ActEntryDTO dto);
}


