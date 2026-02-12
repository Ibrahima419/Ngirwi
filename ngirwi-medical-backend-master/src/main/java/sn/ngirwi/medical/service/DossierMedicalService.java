package sn.ngirwi.medical.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.DossierMedical;
import sn.ngirwi.medical.domain.Patient;
import sn.ngirwi.medical.repository.DossierMedicalRepository;
import sn.ngirwi.medical.repository.PatientRepository;
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
    private final CurrentHospitalProvider currentHospitalProvider;
    private final PatientRepository patientRepository;

    public DossierMedicalService(
        DossierMedicalRepository dossierMedicalRepository,
        DossierMedicalMapper dossierMedicalMapper,
        CurrentHospitalProvider currentHospitalProvider,
        PatientRepository patientRepository
    ) {
        this.dossierMedicalRepository = dossierMedicalRepository;
        this.dossierMedicalMapper = dossierMedicalMapper;
        this.currentHospitalProvider = currentHospitalProvider;
        this.patientRepository = patientRepository;
    }

    /**
     * Save a dossierMedical.
     *
     * @param dossierMedicalDTO the entity to save.
     * @return the persisted entity.
     */
    public DossierMedicalDTO save(DossierMedicalDTO dossierMedicalDTO) {
        log.debug("Request to save DossierMedical : {}", dossierMedicalDTO);
        Long patientId = dossierMedicalDTO.getPatient() != null ? dossierMedicalDTO.getPatient().getId() : null;
        if (patientId == null) {
            throw new IllegalArgumentException("patientId is required");
        }
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Patient not found id=" + patientId));
        assertSameHospital(patient.getHospitalId());
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
        if (dossierMedicalDTO.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }
        dossierMedicalRepository
            .findById(dossierMedicalDTO.getId())
            .ifPresent(existing -> assertSameHospital(existing.getPatient() != null ? existing.getPatient().getHospitalId() : null));
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
                assertSameHospital(existingDossierMedical.getPatient() != null ? existingDossierMedical.getPatient().getHospitalId() : null);
                dossierMedicalMapper.partialUpdate(existingDossierMedical, dossierMedicalDTO);

                return existingDossierMedical;
            })
            .map(dossierMedicalRepository::save)
            .map(dossierMedicalMapper::toDto);
    }

    private void assertSameHospital(Long entityHospitalId) {
        currentHospitalProvider
            .getCurrentHospitalId()
            .ifPresent(current -> {
                if (entityHospitalId != null && !java.util.Objects.equals(entityHospitalId, current)) {
                    throw new AccessDeniedException("Access denied: resource not in your hospital");
                }
            });
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
        return currentHospitalProvider
            .getCurrentHospitalId()
            .map(hid -> dossierMedicalRepository.findAllByPatient_HospitalId(hid, pageable))
            .orElseGet(() -> dossierMedicalRepository.findAll(pageable))
            .map(dossierMedicalMapper::toDto);
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
        return currentHospitalProvider
            .getCurrentHospitalId()
            .map(hid -> dossierMedicalRepository.findByIdAndPatient_HospitalId(id, hid))
            .orElseGet(() -> dossierMedicalRepository.findById(id))
            .map(dossierMedicalMapper::toDto);
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
        dossierMedicalRepository
            .findById(id)
            .ifPresent(existing -> assertSameHospital(existing.getPatient() != null ? existing.getPatient().getHospitalId() : null));
        dossierMedicalRepository.deleteById(id);
    }
}
