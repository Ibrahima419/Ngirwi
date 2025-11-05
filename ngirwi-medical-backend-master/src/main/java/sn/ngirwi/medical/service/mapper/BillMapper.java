package sn.ngirwi.medical.service.mapper;

import org.mapstruct.*;
import sn.ngirwi.medical.domain.Bill;
import sn.ngirwi.medical.domain.Patient;
import sn.ngirwi.medical.service.dto.BillDTO;
import sn.ngirwi.medical.service.dto.PatientDTO;

/**
 * Mapper for the entity {@link Bill} and its DTO {@link BillDTO}.
 */
@Mapper(componentModel = "spring")
public interface BillMapper extends EntityMapper<BillDTO, Bill> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientId")
    BillDTO toDto(Bill s);

    @Named("patientId")
    //@BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientDTO toDtoPatientId(Patient patient);
}
