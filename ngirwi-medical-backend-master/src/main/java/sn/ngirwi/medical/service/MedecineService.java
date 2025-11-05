package sn.ngirwi.medical.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.Medecine;
import sn.ngirwi.medical.repository.MedecineRepository;
import sn.ngirwi.medical.service.dto.MedecineDTO;
import sn.ngirwi.medical.service.mapper.MedecineMapper;

/**
 * Service Implementation for managing {@link Medecine}.
 */
@Service
@Transactional
public class MedecineService {

    private final Logger log = LoggerFactory.getLogger(MedecineService.class);

    private final MedecineRepository medecineRepository;

    private final MedecineMapper medecineMapper;

    public MedecineService(MedecineRepository medecineRepository, MedecineMapper medecineMapper) {
        this.medecineRepository = medecineRepository;
        this.medecineMapper = medecineMapper;
    }

    /**
     * Save a medecine.
     *
     * @param medecineDTO the entity to save.
     * @return the persisted entity.
     */
    public MedecineDTO save(MedecineDTO medecineDTO) {
        log.debug("Request to save Medecine : {}", medecineDTO);
        Medecine medecine = medecineMapper.toEntity(medecineDTO);
        medecine = medecineRepository.save(medecine);
        return medecineMapper.toDto(medecine);
    }

    /**
     * Update a medecine.
     *
     * @param medecineDTO the entity to save.
     * @return the persisted entity.
     */
    public MedecineDTO update(MedecineDTO medecineDTO) {
        log.debug("Request to update Medecine : {}", medecineDTO);
        Medecine medecine = medecineMapper.toEntity(medecineDTO);
        medecine = medecineRepository.save(medecine);
        return medecineMapper.toDto(medecine);
    }

    /**
     * Partially update a medecine.
     *
     * @param medecineDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedecineDTO> partialUpdate(MedecineDTO medecineDTO) {
        log.debug("Request to partially update Medecine : {}", medecineDTO);

        return medecineRepository
            .findById(medecineDTO.getId())
            .map(existingMedecine -> {
                medecineMapper.partialUpdate(existingMedecine, medecineDTO);

                return existingMedecine;
            })
            .map(medecineRepository::save)
            .map(medecineMapper::toDto);
    }

    /**
     * Get all the medecines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MedecineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Medecines");
        return medecineRepository.findAll(pageable).map(medecineMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<MedecineDTO> findAll(Long id) {
        log.debug("Request to get all Medecines");
        return medecineMapper.toDto(medecineRepository.findByOrdonance_Id(id));
    }

    /**
     * Get one medecine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedecineDTO> findOne(Long id) {
        log.debug("Request to get Medecine : {}", id);
        return medecineRepository.findById(id).map(medecineMapper::toDto);
    }

    /**
     * Delete the medecine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Medecine : {}", id);
        medecineRepository.deleteById(id);
    }
}
