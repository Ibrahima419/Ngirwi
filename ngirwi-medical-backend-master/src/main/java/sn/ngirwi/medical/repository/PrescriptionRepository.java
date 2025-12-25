package sn.ngirwi.medical.repository;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ngirwi.medical.domain.Prescription;

/**
 * Spring Data JPA repository for the Prescription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    Page<Prescription> findByAuthorIn(Collection<String> authors, Pageable pageable);

    Page<Prescription> findByConsultation_Patient_HospitalId(Long hospitalId, Pageable pageable);

    java.util.Optional<Prescription> findByIdAndConsultation_Patient_HospitalId(Long id, Long hospitalId);
}
