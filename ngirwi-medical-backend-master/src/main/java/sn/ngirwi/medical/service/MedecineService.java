package sn.ngirwi.medical.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.Medecine;
import sn.ngirwi.medical.repository.MedecineRepository;
import sn.ngirwi.medical.repository.PrescriptionRepository;
import sn.ngirwi.medical.service.dto.MedecineDTO;
import sn.ngirwi.medical.service.mapper.MedecineMapper;
import sn.ngirwi.medical.service.CurrentHospitalProvider;

/**
 * Service Implementation for managing {@link Medecine}.
 */
@Service
@Transactional
public class MedecineService {

    private final Logger log = LoggerFactory.getLogger(MedecineService.class);

    private final MedecineRepository medecineRepository;

    private final MedecineMapper medecineMapper;
    private final CurrentHospitalProvider currentHospitalProvider;
    private final PrescriptionRepository prescriptionRepository;

    public MedecineService(
        MedecineRepository medecineRepository,
        MedecineMapper medecineMapper,
        CurrentHospitalProvider currentHospitalProvider,
        PrescriptionRepository prescriptionRepository
    ) {
        this.medecineRepository = medecineRepository;
        this.medecineMapper = medecineMapper;
        this.currentHospitalProvider = currentHospitalProvider;
        this.prescriptionRepository = prescriptionRepository;
    }

    /**
     * Save a medecine.
     *
     * @param medecineDTO the entity to save.
     * @return the persisted entity.
     */
    public MedecineDTO save(MedecineDTO medecineDTO) {
        log.debug("Request to save Medecine : {}", medecineDTO);
        Long ordId = medecineDTO.getOrdonance() != null ? medecineDTO.getOrdonance().getId() : null;
        if (ordId != null) {
            currentHospitalProvider
                .getCurrentHospitalId()
                .ifPresent(hid -> {
                    // ensure prescription belongs to this hospital
                    prescriptionRepository
                        .findByIdAndConsultation_Patient_HospitalId(ordId, hid)
                        .orElseThrow(() -> new AccessDeniedException("Access denied: prescription not in your hospital"));
                });
        }
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
        if (medecineDTO.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }
        // ensure existing belongs to current hospital
        currentHospitalProvider
            .getCurrentHospitalId()
            .ifPresent(hid -> {
                Medecine existing = medecineRepository
                    .findById(medecineDTO.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Medecine not found id=" + medecineDTO.getId()));
                Long existingHid = existing.getOrdonance() != null &&
                        existing.getOrdonance().getConsultation() != null &&
                        existing.getOrdonance().getConsultation().getPatient() != null
                    ? existing.getOrdonance().getConsultation().getPatient().getHospitalId()
                    : null;
                if (existingHid != null && !java.util.Objects.equals(existingHid, hid)) {
                    throw new AccessDeniedException("Access denied: resource not in your hospital");
                }
            });
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
                currentHospitalProvider
                    .getCurrentHospitalId()
                    .ifPresent(hid -> {
                        Long existingHid = existingMedecine.getOrdonance() != null &&
                                existingMedecine.getOrdonance().getConsultation() != null &&
                                existingMedecine.getOrdonance().getConsultation().getPatient() != null
                            ? existingMedecine.getOrdonance().getConsultation().getPatient().getHospitalId()
                            : null;
                        if (existingHid != null && !java.util.Objects.equals(existingHid, hid)) {
                            throw new AccessDeniedException("Access denied: resource not in your hospital");
                        }
                    });
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
        return currentHospitalProvider
            .getCurrentHospitalId()
            .map(hid -> medecineRepository.findByOrdonance_Consultation_Patient_HospitalId(hid, pageable))
            .orElseGet(() -> medecineRepository.findAll(pageable))
            .map(medecineMapper::toDto);
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
        return currentHospitalProvider
            .getCurrentHospitalId()
            .map(hid -> medecineRepository.findByIdAndOrdonance_Consultation_Patient_HospitalId(id, hid))
            .orElseGet(() -> medecineRepository.findById(id))
            .map(medecineMapper::toDto);
    }

    /**
     * Delete the medecine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Medecine : {}", id);
        currentHospitalProvider
            .getCurrentHospitalId()
            .ifPresent(hid -> {
                Medecine existing = medecineRepository
                    .findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Medecine not found id=" + id));
                Long existingHid = existing.getOrdonance() != null &&
                        existing.getOrdonance().getConsultation() != null &&
                        existing.getOrdonance().getConsultation().getPatient() != null
                    ? existing.getOrdonance().getConsultation().getPatient().getHospitalId()
                    : null;
                if (existingHid != null && !java.util.Objects.equals(existingHid, hid)) {
                    throw new AccessDeniedException("Access denied: resource not in your hospital");
                }
            });
        medecineRepository.deleteById(id);
    }
}
