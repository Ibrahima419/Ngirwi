package sn.ngirwi.medical.service.mapper;

import org.mapstruct.*;
import sn.ngirwi.medical.domain.Bill;
import sn.ngirwi.medical.domain.BillElement;
import sn.ngirwi.medical.service.dto.BillDTO;
import sn.ngirwi.medical.service.dto.BillElementDTO;

/**
 * Mapper for the entity {@link BillElement} and its DTO {@link BillElementDTO}.
 */
@Mapper(componentModel = "spring")
public interface BillElementMapper extends EntityMapper<BillElementDTO, BillElement> {
    @Mapping(target = "bill", source = "bill", qualifiedByName = "billId")
    BillElementDTO toDto(BillElement s);

    @Named("billId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BillDTO toDtoBillId(Bill bill);
}
