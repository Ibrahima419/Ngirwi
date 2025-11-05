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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.Consultation;
import sn.ngirwi.medical.domain.User;
import sn.ngirwi.medical.repository.ConsultationRepository;
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

    public ConsultationService(
        ConsultationRepository consultationRepository,
        ConsultationMapper consultationMapper,
        UserRepository userRepository
    ) {
        this.consultationRepository = consultationRepository;
        this.consultationMapper = consultationMapper;
        this.userRepository = userRepository;
    }

    /**
     * Save a consultation.
     *
     * @param consultationDTO the entity to save.
     * @return the persisted entity.
     */
    public ConsultationDTO save(ConsultationDTO consultationDTO) {
        log.debug("Request to save Consultation : {}", consultationDTO);
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
                consultationMapper.partialUpdate(existingConsultation, consultationDTO);

                return existingConsultation;
            })
            .map(consultationRepository::save)
            .map(consultationMapper::toDto);
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
        return consultationRepository.findAll(pageable).map(consultationMapper::toDto);
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
        return consultationRepository.findOneWithEagerRelationships(id).map(consultationMapper::toDto);
    }

    /**
     * Delete the consultation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Consultation : {}", id);
        consultationRepository.deleteById(id);
    }
}
