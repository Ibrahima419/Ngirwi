package sn.ngirwi.medical.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;
import sn.ngirwi.medical.domain.MiniConsultation;
import sn.ngirwi.medical.domain.SurveillanceSheet;
import sn.ngirwi.medical.domain.enumeration.HospitalisationStatus;
import sn.ngirwi.medical.repository.MiniConsultationRepository;
import sn.ngirwi.medical.repository.SurveillanceSheetRepository;
import sn.ngirwi.medical.service.dto.MiniConsultationDTO;
import sn.ngirwi.medical.service.mapper.MiniConsultationMapper;
import sn.ngirwi.medical.service.CurrentHospitalProvider;

@Service
@Transactional
public class MiniConsultationService {

    private final MiniConsultationRepository miniConsultationRepository;
    private final SurveillanceSheetRepository surveillanceSheetRepository;
    private final MiniConsultationMapper miniConsultationMapper;
    private final CurrentHospitalProvider currentHospitalProvider;

    public MiniConsultationService(
        MiniConsultationRepository miniConsultationRepository,
        SurveillanceSheetRepository surveillanceSheetRepository,
        MiniConsultationMapper miniConsultationMapper,
        CurrentHospitalProvider currentHospitalProvider
    ) {
        this.miniConsultationRepository = miniConsultationRepository;
        this.surveillanceSheetRepository = surveillanceSheetRepository;
        this.miniConsultationMapper = miniConsultationMapper;
        this.currentHospitalProvider = currentHospitalProvider;
    }

    public MiniConsultationDTO save(MiniConsultationDTO dto) {
        // Vérifier si la SurveillanceSheet existe
        if (dto.getSurveillanceSheetId() == null || !surveillanceSheetRepository.existsById(dto.getSurveillanceSheetId())) {
            throw new IllegalArgumentException("Invalid surveillanceSheetId: " + dto.getSurveillanceSheetId());
        }

        // Interdiction si l'hospitalisation liée est clôturée
        SurveillanceSheet sheet = surveillanceSheetRepository
            .findById(dto.getSurveillanceSheetId())
            .orElseThrow(() -> new IllegalArgumentException("SurveillanceSheet not found: id=" + dto.getSurveillanceSheetId()));
        currentHospitalProvider
            .getCurrentHospitalId()
            .ifPresent(hid -> {
                Long sheetHospital = sheet.getHospitalisation() != null && sheet.getHospitalisation().getPatient() != null
                    ? sheet.getHospitalisation().getPatient().getHospitalId()
                    : null;
                if (sheetHospital != null && !sheetHospital.equals(hid)) {
                    throw new IllegalStateException("Access denied: surveillance sheet not in your hospital");
                }
            });
        if (sheet.getHospitalisation() != null && sheet.getHospitalisation().getStatus() == HospitalisationStatus.DONE) {
            throw new IllegalStateException("Impossible d'ajouter une mini-consultation: hospitalisation clôturée");
        }

        MiniConsultation entity = miniConsultationMapper.toEntity(dto);
        entity = miniConsultationRepository.save(entity);
        return miniConsultationMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public Optional<MiniConsultationDTO> findOne(Long id) {
        return currentHospitalProvider
            .getCurrentHospitalId()
            .map(hid ->
                miniConsultationRepository
                    .findById(id)
                    .filter(mc -> {
                        Long pid = mc.getSurveillanceSheet() != null &&
                                mc.getSurveillanceSheet().getHospitalisation() != null &&
                                mc.getSurveillanceSheet().getHospitalisation().getPatient() != null
                            ? mc.getSurveillanceSheet().getHospitalisation().getPatient().getHospitalId()
                            : null;
                        return pid == null || hid.equals(pid);
                    })
            )
            .orElseGet(() -> miniConsultationRepository.findById(id))
            .map(miniConsultationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<MiniConsultationDTO> findAll() {
        return currentHospitalProvider
            .getCurrentHospitalId()
            .map(hid -> miniConsultationRepository.findBySurveillanceSheet_Hospitalisation_Patient_HospitalId(hid))
            .orElseGet(miniConsultationRepository::findAll)
            .stream()
            .map(miniConsultationMapper::toDto)
            .collect(Collectors.toList());
    }

    public void delete(Long id) {
        // enforce tenant access before delete
        findOne(id).ifPresent(x -> { /* already filtered by tenant in findOne */ });
        currentHospitalProvider
            .getCurrentHospitalId()
            .ifPresent(hid -> {
                MiniConsultation mc = miniConsultationRepository
                    .findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("MiniConsultation not found: id=" + id));
                Long pid = mc.getSurveillanceSheet() != null &&
                        mc.getSurveillanceSheet().getHospitalisation() != null &&
                        mc.getSurveillanceSheet().getHospitalisation().getPatient() != null
                    ? mc.getSurveillanceSheet().getHospitalisation().getPatient().getHospitalId()
                    : null;
                if (pid != null && !pid.equals(hid)) {
                    throw new AccessDeniedException("Access denied: resource not in your hospital");
                }
            });
        miniConsultationRepository.deleteById(id);
    }

    /**
     * Chercher la mini-consultation liée à une surveillance sheet.
     */
    @Transactional(readOnly = true)
    public java.util.List<MiniConsultationDTO> findBySurveillanceSheet(Long surveillanceSheetId) {
        // sécurise l'accès par hôpital si contexte présent
        currentHospitalProvider
            .getCurrentHospitalId()
            .ifPresent(hid -> {
                surveillanceSheetRepository
                    .findById(surveillanceSheetId)
                    .ifPresent(sheet -> {
                        Long sheetHospital = sheet.getHospitalisation() != null && sheet.getHospitalisation().getPatient() != null
                            ? sheet.getHospitalisation().getPatient().getHospitalId()
                            : null;
                        if (sheetHospital != null && !sheetHospital.equals(hid)) {
                            throw new IllegalStateException("Access denied: surveillance sheet not in your hospital");
                        }
                    });
            });

        return miniConsultationRepository
            .findBySurveillanceSheet_Id(surveillanceSheetId)
            .stream()
            .map(miniConsultationMapper::toDto)
            .collect(java.util.stream.Collectors.toList());
    }
}
