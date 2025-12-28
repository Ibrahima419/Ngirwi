package sn.ngirwi.medical.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ngirwi.medical.domain.Hospitalisation;
import sn.ngirwi.medical.domain.enumeration.HospitalisationStatus;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Hospitalisation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HospitalisationRepository extends JpaRepository<Hospitalisation, Long> {
    boolean existsByPatientIdAndStatus(Long patientId, HospitalisationStatus status);

    Page<Hospitalisation> findAllByPatient_Id(Long patientId, Pageable pageable);

    @Query(
        "select h from Hospitalisation h where (:patientId is null or h.patient.id = :patientId) " +
        "and (:status is null or h.status = :status) " +
        "and (:from is null or h.entryDate >= :from) " +
        "and (:to is null or h.entryDate <= :to)"
    )
    Page<Hospitalisation> search(
        @Param("patientId") Long patientId,
        @Param("status") HospitalisationStatus status,
        @Param("from") Instant from,
        @Param("to") Instant to,
        Pageable pageable
    );
    @Query("SELECT h FROM Hospitalisation h " +
        "LEFT JOIN FETCH h.patient " +
        "LEFT JOIN FETCH h.surveillanceSheets " +
        "WHERE h.id = :id")
    Optional<Hospitalisation> findByIdWithSheets(@Param("id") Long id);

}
