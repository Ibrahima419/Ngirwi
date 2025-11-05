package sn.ngirwi.medical.service;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.Hospitalisation;
import sn.ngirwi.medical.domain.enumeration.HospitalisationStatus;
import sn.ngirwi.medical.repository.HospitalisationRepository;
import sn.ngirwi.medical.domain.MiniConsultation;
import sn.ngirwi.medical.domain.SurveillanceSheet;
import sn.ngirwi.medical.repository.SurveillanceSheetRepository;
import sn.ngirwi.medical.service.dto.SurveillanceSheetDTO;
import sn.ngirwi.medical.service.mapper.SurveillanceSheetMapper;

/**
 * Service for managing {@link SurveillanceSheet}.
 */
@Service
@Transactional
public class SurveillanceSheetService {

    private final Logger log = LoggerFactory.getLogger(SurveillanceSheetService.class);

    private final SurveillanceSheetRepository surveillanceSheetRepository;
    private final HospitalisationRepository hospitalisationRepository;
    private final SurveillanceSheetMapper surveillanceSheetMapper;

    public SurveillanceSheetService(
        SurveillanceSheetRepository surveillanceSheetRepository,
        SurveillanceSheetMapper surveillanceSheetMapper,
        HospitalisationRepository hospitalisationRepository
    ) {
        this.surveillanceSheetRepository = surveillanceSheetRepository;
        this.surveillanceSheetMapper = surveillanceSheetMapper;
        this.hospitalisationRepository = hospitalisationRepository;
    }

    /**
     * Create/save a SurveillanceSheet.
     * Enforce uniqueness (hospitalisationId, sheetDate).
     */
    public SurveillanceSheetDTO save(SurveillanceSheetDTO dto) {
        Objects.requireNonNull(dto, "dto must not be null");
        if (dto.getHospitalisationId() == null) {
            throw new IllegalArgumentException("hospitalisationId est obligatoire");
        }
        if (dto.getSheetDate() == null) {
            throw new IllegalArgumentException("sheetDate est obligatoire");
        }

        // Interdiction si hospitalisation clôturée
        Hospitalisation hosp = hospitalisationRepository
            .findById(dto.getHospitalisationId())
            .orElseThrow(() -> new NoSuchElementException("Hospitalisation introuvable: id=" + dto.getHospitalisationId()));
        if (hosp.getStatus() == HospitalisationStatus.DONE) {
            throw new IllegalStateException("Impossible d'ajouter une fiche: hospitalisation clôturée");
        }

        // Unicité applicative
        if (surveillanceSheetRepository.existsByHospitalisationIdAndSheetDate(dto.getHospitalisationId(), dto.getSheetDate())) {
            throw new IllegalStateException("Une fiche existe déjà pour cette hospitalisation et cette date");
        }

        SurveillanceSheet entity = surveillanceSheetMapper.toEntity(dto);
        // Le mapper positionne uniquement l'hospitalisation via l'ID.

        log.debug("Saving SurveillanceSheet (hospitalisationId={}, sheetDate={})", dto.getHospitalisationId(), dto.getSheetDate());

        try {
            entity = surveillanceSheetRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            // Filet de sécurité si la contrainte DB déclenche malgré le check applicatif
            throw new IllegalStateException("Contrainte d'unicité violée (hospitalisationId, sheetDate)", e);
        }
        return surveillanceSheetMapper.toDto(entity);
    }

    /**
     * Full update.
     * Vérifie conflit d'unicité si hospitalisationId/sheetDate changent.
     */
    public SurveillanceSheetDTO update(SurveillanceSheetDTO dto) {
        Objects.requireNonNull(dto, "dto must not be null");
        if (dto.getId() == null) {
            throw new IllegalArgumentException("id est obligatoire pour une mise à jour");
        }

        SurveillanceSheet existing = surveillanceSheetRepository
            .findById(dto.getId())
            .orElseThrow(() -> new NoSuchElementException("SurveillanceSheet introuvable: id=" + dto.getId()));

        Long newHospId = dto.getHospitalisationId() != null
            ? dto.getHospitalisationId()
            : (existing.getHospitalisation() != null ? existing.getHospitalisation().getId() : null);
        LocalDate newDate = dto.getSheetDate() != null ? dto.getSheetDate() : existing.getSheetDate();

        if (newHospId == null) {
            throw new IllegalArgumentException("hospitalisationId est obligatoire");
        }
        if (newDate == null) {
            throw new IllegalArgumentException("sheetDate est obligatoire");
        }

        // Si (hospitalisationId, sheetDate) cible appartient à une autre fiche -> conflit
        surveillanceSheetRepository
            .findByHospitalisationIdAndSheetDate(newHospId, newDate)
            .filter(other -> !Objects.equals(other.getId(), existing.getId()))
            .ifPresent(other -> {
                throw new IllegalStateException("Conflit d'unicité: une fiche existe déjà pour cette date sur cette hospitalisation");
            });

        SurveillanceSheet toSave = surveillanceSheetMapper.toEntity(dto);
        // Important : le DTO ne transporte plus les mini-consultations ni les prescriptions.

        log.debug("Updating SurveillanceSheet id={}, hospitalisationId={}, sheetDate={}", dto.getId(), newHospId, newDate);

        try {
            toSave = surveillanceSheetRepository.save(toSave);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Contrainte d'unicité violée (hospitalisationId, sheetDate)", e);
        }
        return surveillanceSheetMapper.toDto(toSave);
    }

    /**
     * Partial update (PATCH).
     * N'applique que les champs non-nuls du DTO.
     * Vérifie l'unicité si (hospitalisationId ou sheetDate) changent.
     */
    public Optional<SurveillanceSheetDTO> partialUpdate(SurveillanceSheetDTO dto) {
        Objects.requireNonNull(dto, "dto must not be null");
        if (dto.getId() == null) {
            throw new IllegalArgumentException("id est obligatoire pour un partialUpdate");
        }

        return surveillanceSheetRepository
            .findById(dto.getId())
            .map(existing -> {
                // Cible potentielle après patch (utilise existing si null côté DTO)
                Long targetHospId = dto.getHospitalisationId() != null
                    ? dto.getHospitalisationId()
                    : (existing.getHospitalisation() != null ? existing.getHospitalisation().getId() : null);

                LocalDate targetDate = dto.getSheetDate() != null ? dto.getSheetDate() : existing.getSheetDate();

                if (targetHospId == null) {
                    throw new IllegalArgumentException("hospitalisationId est obligatoire");
                }
                if (targetDate == null) {
                    throw new IllegalArgumentException("sheetDate est obligatoire");
                }

                // Unicité si le couple change ou reste identique
                surveillanceSheetRepository
                    .findByHospitalisationIdAndSheetDate(targetHospId, targetDate)
                    .filter(other -> !Objects.equals(other.getId(), existing.getId()))
                    .ifPresent(other -> {
                        throw new IllegalStateException("Conflit d'unicité: fiche déjà existante pour cette date");
                    });

                // Appliquer les champs non-nuls
                if (dto.getSheetDate() != null) existing.setSheetDate(dto.getSheetDate());
                if (dto.getTemperature() != null) existing.setTemperature(dto.getTemperature());
                if (dto.getSystolicBP() != null) existing.setSystolicBP(dto.getSystolicBP());
                if (dto.getDiastolicBP() != null) existing.setDiastolicBP(dto.getDiastolicBP());
                if (dto.getPulseRate() != null) existing.setPulseRate(dto.getPulseRate());
                if (dto.getRespirationRate() != null) existing.setRespirationRate(dto.getRespirationRate());
                if (dto.getSpo2() != null) existing.setSpo2(dto.getSpo2());
                if (dto.getNursingNotes() != null) existing.setNursingNotes(dto.getNursingNotes());
                if (dto.getMedicalObservations() != null) existing.setMedicalObservations(dto.getMedicalObservations());
                if (dto.getActsPerformed() != null) existing.setActsPerformed(dto.getActsPerformed());
                if (dto.getAdministeredMedication() != null) existing.setAdministeredMedication(dto.getAdministeredMedication());

                if (dto.getHospitalisationId() != null) {
                    Hospitalisation h = new Hospitalisation();
                    h.setId(dto.getHospitalisationId());
                    existing.setHospitalisation(h);
                }

                // Mini-consultations et prescriptions non gérées par ce DTO/service.

                return existing;
            })
            .map(surveillanceSheetRepository::save)
            .map(surveillanceSheetMapper::toDto);
    }

    // ----------- Queries ------------

    @Transactional(readOnly = true)
    public Page<SurveillanceSheetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SurveillanceSheets");
        return surveillanceSheetRepository.findAll(pageable).map(surveillanceSheetMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<SurveillanceSheetDTO> findByHospitalisation(Long hospitalisationId, Pageable pageable) {
        Objects.requireNonNull(hospitalisationId, "hospitalisationId must not be null");
        log.debug("Request to get SurveillanceSheets by hospitalisationId={}", hospitalisationId);
        return surveillanceSheetRepository.findByHospitalisationId(hospitalisationId, pageable).map(surveillanceSheetMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<SurveillanceSheetDTO> findOne(Long id) {
        log.debug("Request to get SurveillanceSheet id={}", id);
        return surveillanceSheetRepository.findById(id).map(surveillanceSheetMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<SurveillanceSheetDTO> findByHospitalisationAndDate(Long hospitalisationId, LocalDate sheetDate) {
        Objects.requireNonNull(hospitalisationId, "hospitalisationId must not be null");
        Objects.requireNonNull(sheetDate, "sheetDate must not be null");
        return surveillanceSheetRepository
            .findByHospitalisationIdAndSheetDate(hospitalisationId, sheetDate)
            .map(surveillanceSheetMapper::toDto);
    }

    // ----------- Delete ------------

    public void delete(Long id) {
        log.debug("Request to delete SurveillanceSheet id={}", id);
        surveillanceSheetRepository.deleteById(id);
        // orphanRemoval=true supprime MiniConsultation; ManyToMany nettoie la table de jointure.
    }
}
