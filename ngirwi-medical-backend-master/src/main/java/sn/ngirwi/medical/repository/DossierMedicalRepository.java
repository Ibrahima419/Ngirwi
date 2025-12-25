package sn.ngirwi.medical.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ngirwi.medical.domain.DossierMedical;

/**
 * Spring Data JPA repository for the DossierMedical entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Long> {
    Optional<DossierMedical> findByPatient_Id(Long id);

    long deleteByPatient_Id(Long id);

    org.springframework.data.domain.Page<DossierMedical> findAllByPatient_HospitalId(
        Long hospitalId,
        org.springframework.data.domain.Pageable pageable
    );

    Optional<DossierMedical> findByIdAndPatient_HospitalId(Long id, Long hospitalId);
}
