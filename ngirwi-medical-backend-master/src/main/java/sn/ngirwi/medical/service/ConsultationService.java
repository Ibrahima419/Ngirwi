package sn.ngirwi.medical.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.Consultation;
import sn.ngirwi.medical.domain.User;
import sn.ngirwi.medical.domain.Patient;
import sn.ngirwi.medical.repository.ConsultationRepository;
import sn.ngirwi.medical.repository.PatientRepository;
import sn.ngirwi.medical.repository.UserRepository;
import sn.ngirwi.medical.service.dto.ConsultationDTO;
import sn.ngirwi.medical.service.mapper.ConsultationMapper;

/**
 * Service Implementation for managing {@link Consultation}.
 */
@Service
@Transactional
public class ConsultationService {

    private final Logger log = LoggerFactory.getLogger(ConsultationService.class);

    private final ConsultationRepository consultationRepository;

    private final ConsultationMapper consultationMapper;
    private final UserRepository userRepository;
    private final CurrentHospitalProvider currentHospitalProvider;
    private final PatientRepository patientRepository;

    public ConsultationService(
        ConsultationRepository consultationRepository,
        ConsultationMapper consultationMapper,
        UserRepository userRepository,
        CurrentHospitalProvider currentHospitalProvider,
        PatientRepository patientRepository
    ) {
        this.consultationRepository = consultationRepository;
        this.consultationMapper = consultationMapper;
        this.userRepository = userRepository;
        this.currentHospitalProvider = currentHospitalProvider;
        this.patientRepository = patientRepository;
    }

    /**
     * Save a consultation.
     *
     * @param consultationDTO the entity to save.
     * @return the persisted entity.
     */
    public ConsultationDTO save(ConsultationDTO consultationDTO) {
        log.debug("Request to save Consultation : {}", consultationDTO);
        Long patientId = consultationDTO.getPatient() != null ? consultationDTO.getPatient().getId() : null;
        if (patientId == null) {
            throw new IllegalArgumentException("patientId is required");
        }
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Patient not found id=" + patientId));
        assertSameHospital(patient.getHospitalId());
        Consultation consultation = consultationMapper.toEntity(consultationDTO);
        consultation = consultationRepository.save(consultation);
        return consultationMapper.toDto(consultation);
    }

    /**
     * Update a consultation.
     *
     * @param consultationDTO the entity to save.
     * @return the persisted entity.
     */
    public ConsultationDTO update(ConsultationDTO consultationDTO) {
        log.debug("Request to update Consultation : {}", consultationDTO);
        if (consultationDTO.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }
        consultationRepository
            .findById(consultationDTO.getId())
            .ifPresent(existing -> assertSameHospital(existing.getPatient() != null ? existing.getPatient().getHospitalId() : null));
        Consultation consultation = consultationMapper.toEntity(consultationDTO);
        consultation = consultationRepository.save(consultation);
        return consultationMapper.toDto(consultation);
    }

    /**
     * Partially update a consultation.
     *
     * @param consultationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConsultationDTO> partialUpdate(ConsultationDTO consultationDTO) {
        log.debug("Request to partially update Consultation : {}", consultationDTO);

        return consultationRepository
            .findById(consultationDTO.getId())
            .map(existingConsultation -> {
                assertSameHospital(existingConsultation.getPatient() != null ? existingConsultation.getPatient().getHospitalId() : null);
                consultationMapper.partialUpdate(existingConsultation, consultationDTO);

                return existingConsultation;
            })
            .map(consultationRepository::save)
            .map(consultationMapper::toDto);
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
     * Get all the consultations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConsultationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Consultations");
        return currentHospitalProvider
            .getCurrentHospitalId()
            .map(hid -> consultationRepository.findByPatient_HospitalId(hid, pageable))
            .orElseGet(() -> consultationRepository.findAll(pageable))
            .map(consultationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ConsultationDTO> findAll(Pageable pageable, Long id) {
        log.debug("Request to get all Consultations by hospital " + id);
        List<User> users = userRepository.findByHospitalId(id);
        List<String> logins = new ArrayList<>();
        if (users.size() > 0) {
            for (User user : users) {
                log.debug(user.toString());
                logins.add(user.getLogin());
            }
        }
        return consultationRepository.findByAuthorIn(logins, pageable).map(consultationMapper::toDto);
    }

    /**
     * Get all the consultations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ConsultationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return consultationRepository.findAllWithEagerRelationships(pageable).map(consultationMapper::toDto);
    }

    /**
     *  Get all the consultations where Ordonance is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ConsultationDTO> findAllWhereOrdonanceIsNull() {
        log.debug("Request to get all consultations where Ordonance is null");
        return StreamSupport
            .stream(consultationRepository.findAll().spliterator(), false)
            .filter(consultation -> consultation.getOrdonance() == null)
            .map(consultationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one consultation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConsultationDTO> findOne(Long id) {
        log.debug("Request to get Consultation : {}", id);
        return currentHospitalProvider
            .getCurrentHospitalId()
            .map(hid -> consultationRepository.findByIdAndPatient_HospitalId(id, hid))
            .orElseGet(() -> consultationRepository.findOneWithToOneRelationships(id))
            .map(consultationMapper::toDto);
    }

    /**
     * Delete the consultation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Consultation : {}", id);
        consultationRepository
            .findById(id)
            .ifPresent(existing -> assertSameHospital(existing.getPatient() != null ? existing.getPatient().getHospitalId() : null));
        consultationRepository.deleteById(id);
    }
}
