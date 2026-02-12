package sn.ngirwi.medical.repository;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ngirwi.medical.domain.Hospitalisation;
import sn.ngirwi.medical.domain.enumeration.HospitalisationStatus;

/**
 * Spring Data JPA repository for the Hospitalisation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HospitalisationRepository extends JpaRepository<Hospitalisation, Long> {
    boolean existsByPatientIdAndStatus(Long patientId, HospitalisationStatus status);

    Page<Hospitalisation> findAllByPatient_Id(Long patientId, Pageable pageable);

    Optional<Hospitalisation> findFirstByPatient_IdAndStatusInOrderByEntryDateDesc(Long patientId, Collection<HospitalisationStatus> statuses);

    Optional<Hospitalisation> findFirstByPatient_IdOrderByEntryDateDesc(Long patientId);

    @Query(
        "select h from Hospitalisation h where (cast(:patientId as long) is null or h.patient.id = :patientId) " +
        "and (cast(:status as string) is null or h.status = :status) " +
        "and (cast(:from as timestamp) is null or h.entryDate >= :from) " +
        "and (cast(:to as timestamp) is null or h.entryDate <= :to) " +
        "and (cast(:hospitalId as long) is null or h.patient.hospitalId = :hospitalId)"
    )
    Page<Hospitalisation> search(
        @Param("patientId") Long patientId,
        @Param("status") HospitalisationStatus status,
        @Param("from") Instant from,
        @Param("to") Instant to,
        @Param("hospitalId") Long hospitalId,
        Pageable pageable
    );
}
