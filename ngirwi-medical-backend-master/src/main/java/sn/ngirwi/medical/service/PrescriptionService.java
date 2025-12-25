package sn.ngirwi.medical.service;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.*;
import sn.ngirwi.medical.repository.ConsultationRepository;
import sn.ngirwi.medical.repository.MedecineRepository;
import sn.ngirwi.medical.repository.PrescriptionRepository;
import sn.ngirwi.medical.repository.UserRepository;
import sn.ngirwi.medical.service.dto.PrescriptionDTO;
import sn.ngirwi.medical.service.mapper.ConsultationMapper;
import sn.ngirwi.medical.service.mapper.PrescriptionMapper;
import sn.ngirwi.medical.service.model.PrescriptionForm;
import sn.ngirwi.medical.service.CurrentHospitalProvider;

/**
 * Service Implementation for managing {@link Prescription}.
 */
@Service
@Transactional
public class PrescriptionService {

    private final Logger log = LoggerFactory.getLogger(PrescriptionService.class);

    private final PrescriptionRepository prescriptionRepository;

    private final PrescriptionMapper prescriptionMapper;
    private final UserRepository userRepository;

    private final ConsultationMapper consultationMapper;

    private final MedecineRepository medecineRepository;
    private final CurrentHospitalProvider currentHospitalProvider;
    private final ConsultationRepository consultationRepository;

    public PrescriptionService(
        PrescriptionRepository prescriptionRepository,
        PrescriptionMapper prescriptionMapper,
        UserRepository userRepository,
        ConsultationMapper consultationMapper,
        MedecineRepository medecineRepository,
        CurrentHospitalProvider currentHospitalProvider,
        ConsultationRepository consultationRepository
    ) {
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionMapper = prescriptionMapper;
        this.userRepository = userRepository;
        this.consultationMapper = consultationMapper;
        this.medecineRepository = medecineRepository;
        this.currentHospitalProvider = currentHospitalProvider;
        this.consultationRepository = consultationRepository;
    }

    /**
     * Save a prescription.
     *
     * @param prescriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public PrescriptionDTO save(PrescriptionDTO prescriptionDTO) {
        log.debug("Request to save Prescription : {}", prescriptionDTO);
        Long consultationId = prescriptionDTO.getConsultation() != null ? prescriptionDTO.getConsultation().getId() : null;
        if (consultationId == null) {
            throw new IllegalArgumentException("consultationId is required");
        }
        Consultation c = consultationRepository.findById(consultationId).orElseThrow(() -> new IllegalArgumentException("Consultation not found id=" + consultationId));
        assertSameHospital(c.getPatient() != null ? c.getPatient().getHospitalId() : null);
        Prescription prescription = prescriptionMapper.toEntity(prescriptionDTO);
        prescription = prescriptionRepository.save(prescription);
        return prescriptionMapper.toDto(prescription);
    }

    /**
     * Update a prescription.
     *
     * @param prescriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public PrescriptionDTO update(PrescriptionDTO prescriptionDTO) {
        log.debug("Request to update Prescription : {}", prescriptionDTO);
        if (prescriptionDTO.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }
        prescriptionRepository
            .findById(prescriptionDTO.getId())
            .ifPresent(existing -> {
                Long hid = existing.getConsultation() != null && existing.getConsultation().getPatient() != null
                    ? existing.getConsultation().getPatient().getHospitalId()
                    : null;
                assertSameHospital(hid);
            });
        Prescription prescription = prescriptionMapper.toEntity(prescriptionDTO);
        prescription = prescriptionRepository.save(prescription);
        return prescriptionMapper.toDto(prescription);
    }

    /**
     * Partially update a prescription.
     *
     * @param prescriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PrescriptionDTO> partialUpdate(PrescriptionDTO prescriptionDTO) {
        log.debug("Request to partially update Prescription : {}", prescriptionDTO);

        return prescriptionRepository
            .findById(prescriptionDTO.getId())
            .map(existingPrescription -> {
                Long hid = existingPrescription.getConsultation() != null && existingPrescription.getConsultation().getPatient() != null
                    ? existingPrescription.getConsultation().getPatient().getHospitalId()
                    : null;
                assertSameHospital(hid);
                prescriptionMapper.partialUpdate(existingPrescription, prescriptionDTO);

                return existingPrescription;
            })
            .map(prescriptionRepository::save)
            .map(prescriptionMapper::toDto);
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
     * Get all the prescriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PrescriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Prescriptions");
        return currentHospitalProvider
            .getCurrentHospitalId()
            .map(hid -> prescriptionRepository.findByConsultation_Patient_HospitalId(hid, pageable))
            .orElseGet(() -> prescriptionRepository.findAll(pageable))
            .map(prescriptionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PrescriptionDTO> findAll(Pageable pageable, Long id) {
        log.debug("Request to get all prescriptions by hospital " + id);
        List<User> users = userRepository.findByHospitalId(id);
        List<String> logins = new ArrayList<>();
        if (users.size() > 0) {
            for (User user : users) {
                log.debug(user.toString());
                logins.add(user.getLogin());
            }
        }
        return prescriptionRepository.findByAuthorIn(logins, pageable).map(prescriptionMapper::toDto);
    }

    /**
     * Get one prescription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PrescriptionDTO> findOne(Long id) {
        log.debug("Request to get Prescription : {}", id);
        return currentHospitalProvider
            .getCurrentHospitalId()
            .map(hid -> prescriptionRepository.findByIdAndConsultation_Patient_HospitalId(id, hid))
            .orElseGet(() -> prescriptionRepository.findById(id))
            .map(prescriptionMapper::toDto);
    }

    /**
     * Delete the prescription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Prescription : {}", id);

        prescriptionRepository
            .findById(id)
            .ifPresent(existing -> {
                Long hid = existing.getConsultation() != null && existing.getConsultation().getPatient() != null
                    ? existing.getConsultation().getPatient().getHospitalId()
                    : null;
                assertSameHospital(hid);
            });

        List<Medecine> medecines = medecineRepository.findByOrdonance_Id(id);

        for (Medecine m : medecines) {
            medecineRepository.deleteById(m.getId());
        }
        prescriptionRepository.deleteById(id);
    }

    public Prescription map(PrescriptionDTO prescriptionDTO) {
        Prescription p = new Prescription();
        //p.setId(prescriptionDTO.getId());
        p.setAuthor(prescriptionDTO.getAuthor());
        p.setCreationDate(prescriptionDTO.getCreationDate());
        Consultation c = consultationMapper.toEntity(prescriptionDTO.getConsultation());
        p.setConsultation(c);
        Set<Medecine> medecines = medecineMapper(prescriptionDTO);
        for (Medecine m : medecines) {
            m.setOrdonance(p);
        }
        p.setMedecines(medecines);
        return p;
    }

    public Set<Medecine> medecineMapper(PrescriptionDTO prescriptionDTO) {
        Set<Medecine> s = new HashSet<>();
        //for (int i = 0; i < prescriptionDTO.getMedecines().toArray().length; i++){
        for (PrescriptionForm f : prescriptionDTO.getMedecines()) {
            Medecine m = new Medecine();
            //m.setId(prescriptionDTO.getId() + i);
            m.setName(f.getName());
            m.setDuration(Long.valueOf(f.getDuration()));
            m.setFrequency(Double.valueOf(f.getFrequency()));
            s.add(m);
        }
        //}

        return s;
    }

    public PrescriptionDTO saveBis(PrescriptionDTO prescriptionDTO) {
        log.debug("Request to save Prescription : {}", prescriptionDTO);

        Prescription prescription = map(prescriptionDTO); // Map DTO to entity

        // Assuming prescriptionDTO has medicines mapped correctly
        for (Medecine medecine : prescription.getMedecines()) {
            medecine.setOrdonance(prescription); // Associate medicine with prescription
            // Remove manual save if cascade persist is configured
            medecineRepository.save(medecine); // Remove this line if cascade persist is configured
        }

        // Save prescription (and associated medicines if cascade persist is configured)
        prescription = prescriptionRepository.save(prescription);

        log.debug(prescription.toString());

        prescriptionDTO.setId(prescription.getId());

        //return prescriptionMapper.toDto(prescription); // Map entity back to DTO
        return prescriptionDTO; // Map entity back to DTO
    }

    public PrescriptionDTO updateBis(PrescriptionDTO prescriptionDTO) {
        log.debug("Request to update Prescription : {}", prescriptionDTO);
        Prescription prescription = prescriptionMapper.toEntity(prescriptionDTO);
        log.debug("VALUES : {}", prescription);
        log.debug("FORM : {}", prescription.getMedecines());

        //prescription = prescriptionRepository.save(prescription);

        for (Medecine medecine : prescription.getMedecines()) {
            if (
                prescription.getId() != null &&
                medecineRepository.existsByNameAndDurationAndFrequencyAndOrdonance_Id(
                    medecine.getName(),
                    medecine.getDuration(),
                    medecine.getFrequency(),
                    prescriptionDTO.getId()
                )
            ) {
                Medecine medecine1 = medecineRepository.findByNameAndDurationAndFrequencyAndOrdonance_Id(
                    medecine.getName(),
                    medecine.getDuration(),
                    medecine.getFrequency(),
                    prescriptionDTO.getId()
                );
                medecineRepository.deleteById(medecine1.getId());
            }
            medecine.setOrdonance(prescription);
            medecineRepository.save(medecine);
        }

        //return prescriptionMapper.toDto(prescription);
        return prescriptionDTO;
    }
}
