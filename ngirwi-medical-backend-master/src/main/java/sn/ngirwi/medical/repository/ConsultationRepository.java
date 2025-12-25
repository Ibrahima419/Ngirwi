package sn.ngirwi.medical.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ngirwi.medical.domain.Consultation;

/**
 * Spring Data JPA repository for the Consultation entity.
 */
@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    default Optional<Consultation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Consultation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Consultation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct consultation from Consultation consultation left join fetch consultation.patient",
        countQuery = "select count(distinct consultation) from Consultation consultation"
    )
    Page<Consultation> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct consultation from Consultation consultation left join fetch consultation.patient")
    List<Consultation> findAllWithToOneRelationships();

    @Query("select consultation from Consultation consultation left join fetch consultation.patient where consultation.id =:id")
    Optional<Consultation> findOneWithToOneRelationships(@Param("id") Long id);

    Page<Consultation> findByAuthorIn(Collection<String> authors, Pageable pageable);

    Page<Consultation> findByPatient_HospitalId(Long hospitalId, Pageable pageable);

    Optional<Consultation> findByIdAndPatient_HospitalId(Long id, Long hospitalId);
}
