package sn.ngirwi.medical.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.Hospital;
import sn.ngirwi.medical.repository.HospitalRepository;

/**
 * Service Implementation for managing {@link Hospital}.
 */
@Service
@Transactional
public class HospitalService {

    private final Logger log = LoggerFactory.getLogger(HospitalService.class);

    private final HospitalRepository hospitalRepository;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    /**
     * Save a hospital.
     *
     * @param hospital the entity to save.
     * @return the persisted entity.
     */
    public Hospital save(Hospital hospital) {
        log.debug("Request to save Hospital : {}", hospital);
        return hospitalRepository.save(hospital);
    }

    /**
     * Update a hospital.
     *
     * @param hospital the entity to save.
     * @return the persisted entity.
     */
    public Hospital update(Hospital hospital) {
        log.debug("Request to update Hospital : {}", hospital);
        return hospitalRepository.save(hospital);
    }

    /**
     * Partially update a hospital.
     *
     * @param hospital the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Hospital> partialUpdate(Hospital hospital) {
        log.debug("Request to partially update Hospital : {}", hospital);

        return hospitalRepository
            .findById(hospital.getId())
            .map(existingHospital -> {
                if (hospital.getName() != null) {
                    existingHospital.setName(hospital.getName());
                }
                if (hospital.getAdress() != null) {
                    existingHospital.setAdress(hospital.getAdress());
                }
                if (hospital.getPhone() != null) {
                    existingHospital.setPhone(hospital.getPhone());
                }
                if (hospital.getLogo() != null) {
                    existingHospital.setLogo(hospital.getLogo());
                }
                if (hospital.getLogoContentType() != null) {
                    existingHospital.setLogoContentType(hospital.getLogoContentType());
                }

                return existingHospital;
            })
            .map(hospitalRepository::save);
    }

    /**
     * Get all the hospitals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Hospital> findAll(Pageable pageable) {
        log.debug("Request to get all Hospitals");
        return hospitalRepository.findAll(pageable);
    }

    /**
     * Get one hospital by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Hospital> findOne(Long id) {
        log.debug("Request to get Hospital : {}", id);
        return hospitalRepository.findById(id);
    }

    /**
     * Delete the hospital by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Hospital : {}", id);
        hospitalRepository.deleteById(id);
    }
}
