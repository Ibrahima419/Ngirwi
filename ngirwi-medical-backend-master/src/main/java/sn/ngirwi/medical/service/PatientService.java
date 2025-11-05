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
import sn.ngirwi.medical.domain.Patient;
import sn.ngirwi.medical.domain.User;
import sn.ngirwi.medical.repository.DossierMedicalRepository;
import sn.ngirwi.medical.repository.PatientRepository;
import sn.ngirwi.medical.repository.UserRepository;
import sn.ngirwi.medical.service.dto.PatientDTO;
import sn.ngirwi.medical.service.mapper.PatientMapper;

/**
 * Service Implementation for managing {@link Patient}.
 */
@Service
@Transactional
public class PatientService {

    private final Logger log = LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;

    private final UserRepository userRepository;

    private final PatientMapper patientMapper;
    private final DossierMedicalRepository dossierMedicalRepository;

    public PatientService(
        PatientRepository patientRepository,
        UserRepository userRepository,
        PatientMapper patientMapper,
        DossierMedicalRepository dossierMedicalRepository
    ) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.patientMapper = patientMapper;
        this.dossierMedicalRepository = dossierMedicalRepository;
    }

    /**
     * Save a patient.
     *
     * @param patientDTO the entity to save.
     * @return the persisted entity.
     */
    public PatientDTO save(PatientDTO patientDTO) {
        log.debug("Request to save Patient : {}", patientDTO);
        Patient patient = patientMapper.toEntity(patientDTO);
        User user = userRepository.findOneByLogin(patient.getAuthor()).get();
        patient.setHospitalId(user.getHospitalId());
        patient = patientRepository.save(patient);
        return patientMapper.toDto(patient);
    }

    /**
     * Update a patient.
     *
     * @param patientDTO the entity to save.
     * @return the persisted entity.
     */
    public PatientDTO update(PatientDTO patientDTO) {
        log.debug("Request to update Patient : {}", patientDTO);
        Patient patient = patientMapper.toEntity(patientDTO);
        patient = patientRepository.save(patient);
        return patientMapper.toDto(patient);
    }

    /**
     * Partially update a patient.
     *
     * @param patientDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PatientDTO> partialUpdate(PatientDTO patientDTO) {
        log.debug("Request to partially update Patient : {}", patientDTO);

        return patientRepository
            .findById(patientDTO.getId())
            .map(existingPatient -> {
                patientMapper.partialUpdate(existingPatient, patientDTO);

                return existingPatient;
            })
            .map(patientRepository::save)
            .map(patientMapper::toDto);
    }

    /**
     * Get all the patients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PatientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Patients");
        return patientRepository.findAll(pageable).map(patientMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PatientDTO> findAll(Pageable pageable, Long id) {
        log.debug("Request to get all Patients by hospital " + id);
        //        List<User> users = userRepository.findByHospitalId(id);
        //        List<String> logins = new ArrayList<>();
        //        if (users.size() > 0){
        //            for (User user : users) {
        //                log.debug(user.toString());
        //                logins.add(user.getLogin());
        //            }
        //        }
        return patientRepository.findByHospitalId(id, pageable).map(patientMapper::toDto);
    }

    /**
     *  Get all the patients where DossierMedical is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PatientDTO> findAllWhereDossierMedicalIsNull() {
        log.debug("Request to get all patients where DossierMedical is null");
        return StreamSupport
            .stream(patientRepository.findAll().spliterator(), false)
            .filter(patient -> patient.getDossierMedical() == null)
            .map(patientMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one patient by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PatientDTO> findOne(Long id) {
        log.debug("Request to get Patient : {}", id);
        return patientRepository.findById(id).map(patientMapper::toDto);
    }

    /**
     * Delete the patient by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Patient : {}", id);
        dossierMedicalRepository.deleteByPatient_Id(id);
        patientRepository.deleteById(id);
    }
}
