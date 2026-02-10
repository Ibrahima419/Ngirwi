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
        for (PrescriptionForm f : prescriptionDTO.getMedecines()) {
            Medecine m = new Medecine();
            m.setId(f.getId()); // Preserve ID for smart update
            m.setName(f.getName());
            // Null-safe conversion: empty/null/non-numeric strings become null (database allows nullable)
            m.setDuration(parseLongSafe(f.getDuration()));
            m.setFrequency(parseDoubleSafe(f.getFrequency()));
            s.add(m);
        }
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
        
        if (prescriptionDTO.getId() == null) {
            throw new IllegalArgumentException("Prescription ID cannot be null for update");
        }
        
        // 1. Load existing prescription from DB (attached to Hibernate session)
        Prescription prescription = prescriptionRepository.findById(prescriptionDTO.getId())
            .orElseThrow(() -> new IllegalArgumentException("Prescription not found id=" + prescriptionDTO.getId()));
        
        // 2. Update scalar fields only (medecines managed separately below)
        prescription.setAuthor(prescriptionDTO.getAuthor());
        prescription.setCreationDate(prescriptionDTO.getCreationDate());
        prescription = prescriptionRepository.save(prescription);
        
        // 3. Get existing medicines from DB
        List<Medecine> existingMedecines = medecineRepository.findByOrdonance_Id(prescriptionDTO.getId());
        
        // 4. Map submitted medicines from DTO (preserves IDs for existing ones)
        Set<Medecine> submittedMedecines = medecineMapper(prescriptionDTO);
        
        // 5. Collect IDs of submitted medicines (for deletion check)
        Set<Long> submittedIds = new HashSet<>();
        for (Medecine m : submittedMedecines) {
            if (m.getId() != null) {
                submittedIds.add(m.getId());
            }
        }
        
        // 6. DELETE medicines that were removed by user
        for (Medecine existing : existingMedecines) {
            if (!submittedIds.contains(existing.getId())) {
                log.debug("Deleting removed medicine: {}", existing.getId());
                medecineRepository.deleteById(existing.getId());
            }
        }
        
        // 7. UPDATE existing or CREATE new medicines
        for (Medecine medecine : submittedMedecines) {
            medecine.setOrdonance(prescription);
            if (medecine.getId() != null) {
                log.debug("Updating existing medicine: {}", medecine.getId());
            } else {
                log.debug("Creating new medicine: {}", medecine.getName());
            }
            medecineRepository.save(medecine);
        }

        prescriptionDTO.setId(prescription.getId());
        return prescriptionDTO;
    }

    /**
     * Parse a string to Long, returning null if the input is null, empty, or not a valid number.
     */
    private static Long parseLongSafe(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parse a string to Double, returning null if the input is null, empty, or not a valid number.
     */
    private static Double parseDoubleSafe(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return Double.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
