package sn.ngirwi.medical.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ngirwi.medical.domain.Medecine;

/**
 * Spring Data JPA repository for the Medecine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedecineRepository extends JpaRepository<Medecine, Long> {
    List<Medecine> findByOrdonance_Id(Long id);

    boolean existsByNameAndDurationAndFrequencyAndOrdonance_Id(String name, Long duration, Double frequency, Long id);

    Medecine findByNameAndDurationAndFrequencyAndOrdonance_Id(String name, Long duration, Double frequency, Long id);
}
