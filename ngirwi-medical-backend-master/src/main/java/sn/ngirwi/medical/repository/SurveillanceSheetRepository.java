package sn.ngirwi.medical.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ngirwi.medical.domain.SurveillanceSheet;

/**
 * Spring Data JPA repository for the SurveillanceSheet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SurveillanceSheetRepository extends JpaRepository<SurveillanceSheet, Long> {
    boolean existsByHospitalisationIdAndSheetDate(Long hospitalisationId, LocalDate sheetDate);
    Page<SurveillanceSheet> findByHospitalisationId(Long hospitalisationId, Pageable pageable);
    Optional<SurveillanceSheet> findByHospitalisationIdAndSheetDate(Long hospitalisationId, LocalDate sheetDate);
    List<SurveillanceSheet> findByHospitalisation_Id(Long id);
}
