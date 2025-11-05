package sn.ngirwi.medical.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ngirwi.medical.domain.Hospital;

/**
 * Spring Data JPA repository for the Hospital entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {}
