package sn.ngirwi.medical.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ngirwi.medical.domain.BillElement;

/**
 * Spring Data JPA repository for the BillElement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillElementRepository extends JpaRepository<BillElement, Long> {
    List<BillElement> findByBill_Id(Long id);

    boolean existsByNameAndPriceAndPercentageAndQuantityAndBill_Id(String name, Double price, Double percentage, Integer quantity, Long id);

    long deleteByNameAndPriceAndPercentageAndQuantityAndBill_Id(String name, Double price, Double percentage, Integer quantity, Long id);

    @Query(
        value = "select COALESCE(sum(CAST(price as decimal(21,2)) * COALESCE(quantity,0) * (1 - COALESCE(percentage,0)/100)),0) from bill_element where bill_id = :billId",
        nativeQuery = true
    )
    java.math.BigDecimal computeTotalByBillId(@org.springframework.data.repository.query.Param("billId") Long billId);

    org.springframework.data.domain.Page<BillElement> findByBill_Patient_HospitalId(Long hospitalId, org.springframework.data.domain.Pageable pageable);

    java.util.Optional<BillElement> findByIdAndBill_Patient_HospitalId(Long id, Long hospitalId);
}
