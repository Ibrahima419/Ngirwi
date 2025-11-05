package sn.ngirwi.medical.repository;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ngirwi.medical.domain.Bill;

/**
 * Spring Data JPA repository for the Bill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Page<Bill> findByAuthorIn(Collection<String> authors, Pageable pageable);
}
