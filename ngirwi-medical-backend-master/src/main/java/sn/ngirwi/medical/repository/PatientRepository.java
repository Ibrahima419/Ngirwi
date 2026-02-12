package sn.ngirwi.medical.repository;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ngirwi.medical.domain.Patient;

/**
 * Spring Data JPA repository for the Patient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findByAuthorIn(Collection<String> authors, Pageable pageable);

    Page<Patient> findByHospitalId(Long hospitalId, Pageable pageable);

    boolean existsByCni(String cni);

    boolean existsByCniAndIdNot(String cni, Long id);
}
