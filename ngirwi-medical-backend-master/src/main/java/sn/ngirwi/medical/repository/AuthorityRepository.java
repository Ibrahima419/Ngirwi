package sn.ngirwi.medical.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.ngirwi.medical.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
