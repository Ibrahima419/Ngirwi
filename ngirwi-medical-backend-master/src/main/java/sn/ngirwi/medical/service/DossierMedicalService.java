package sn.ngirwi.medical.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.DossierMedical;
import sn.ngirwi.medical.repository.DossierMedicalRepository;
import sn.ngirwi.medical.service.dto.DossierMedicalDTO;
import sn.ngirwi.medical.service.mapper.DossierMedicalMapper;

/**
 * Service Implementation for managing {@link DossierMedical}.
 */
@Service
@Transactional
public class DossierMedicalService {

    private final Logger log = LoggerFactory.getLogger(DossierMedicalService.class);

    private final DossierMedicalRepository dossierMedicalRepository;

    private final DossierMedicalMapper dossierMedicalMapper;

    public DossierMedicalService(DossierMedicalRepository dossierMedicalRepository, DossierMedicalMapper dossierMedicalMapper) {
        this.dossierMedicalRepository = dossierMedicalRepository;
        this.dossierMedicalMapper = dossierMedicalMapper;
    }

    /**
     * Save a dossierMedical.
     *
     * @param dossierMedicalDTO the entity to save.
     * @return the persisted entity.
     */
    public DossierMedicalDTO save(DossierMedicalDTO dossierMedicalDTO) {
        log.debug("Request to save DossierMedical : {}", dossierMedicalDTO);
        DossierMedical dossierMedical = dossierMedicalMapper.toEntity(dossierMedicalDTO);
        dossierMedical = dossierMedicalRepository.save(dossierMedical);
        return dossierMedicalMapper.toDto(dossierMedical);
    }

    /**
     * Update a dossierMedical.
     *
     * @param dossierMedicalDTO the entity to save.
     * @return the persisted entity.
     */
    public DossierMedicalDTO update(DossierMedicalDTO dossierMedicalDTO) {
        log.debug("Request to update DossierMedical : {}", dossierMedicalDTO);
        DossierMedical dossierMedical = dossierMedicalMapper.toEntity(dossierMedicalDTO);
        dossierMedical = dossierMedicalRepository.save(dossierMedical);
        return dossierMedicalMapper.toDto(dossierMedical);
    }

    /**
     * Partially update a dossierMedical.
     *
     * @param dossierMedicalDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DossierMedicalDTO> partialUpdate(DossierMedicalDTO dossierMedicalDTO) {
        log.debug("Request to partially update DossierMedical : {}", dossierMedicalDTO);

        return dossierMedicalRepository
            .findById(dossierMedicalDTO.getId())
            .map(existingDossierMedical -> {
                dossierMedicalMapper.partialUpdate(existingDossierMedical, dossierMedicalDTO);

                return existingDossierMedical;
            })
            .map(dossierMedicalRepository::save)
            .map(dossierMedicalMapper::toDto);
    }

    /**
     * Get all the dossierMedicals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DossierMedicalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DossierMedicals");
        return dossierMedicalRepository.findAll(pageable).map(dossierMedicalMapper::toDto);
    }

    /**
     * Get one dossierMedical by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DossierMedicalDTO> findOne(Long id) {
        log.debug("Request to get DossierMedical : {}", id);
        return dossierMedicalRepository.findById(id).map(dossierMedicalMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<DossierMedical> findPatient(Long id) {
        log.debug("Request to get DossierMedical for specific patient: {}", id);
        return dossierMedicalRepository.findByPatient_Id(id);
    }

    /**
     * Delete the dossierMedical by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DossierMedical : {}", id);
        dossierMedicalRepository.deleteById(id);
    }
}
