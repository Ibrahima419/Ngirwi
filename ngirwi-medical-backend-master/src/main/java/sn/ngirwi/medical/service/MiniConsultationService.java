package sn.ngirwi.medical.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.MiniConsultation;
import sn.ngirwi.medical.domain.SurveillanceSheet;
import sn.ngirwi.medical.domain.enumeration.HospitalisationStatus;
import sn.ngirwi.medical.repository.MiniConsultationRepository;
import sn.ngirwi.medical.repository.SurveillanceSheetRepository;
import sn.ngirwi.medical.service.dto.MiniConsultationDTO;
import sn.ngirwi.medical.service.mapper.MiniConsultationMapper;

@Service
@Transactional
public class MiniConsultationService {

    private final MiniConsultationRepository miniConsultationRepository;
    private final SurveillanceSheetRepository surveillanceSheetRepository;
    private final MiniConsultationMapper miniConsultationMapper;

    public MiniConsultationService(
        MiniConsultationRepository miniConsultationRepository,
        SurveillanceSheetRepository surveillanceSheetRepository,
        MiniConsultationMapper miniConsultationMapper
    ) {
        this.miniConsultationRepository = miniConsultationRepository;
        this.surveillanceSheetRepository = surveillanceSheetRepository;
        this.miniConsultationMapper = miniConsultationMapper;
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
        if (sheet.getHospitalisation() != null && sheet.getHospitalisation().getStatus() == HospitalisationStatus.DONE) {
            throw new IllegalStateException("Impossible d'ajouter une mini-consultation: hospitalisation clôturée");
        }

        MiniConsultation entity = miniConsultationMapper.toEntity(dto);
        entity = miniConsultationRepository.save(entity);
        return miniConsultationMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public Optional<MiniConsultationDTO> findOne(Long id) {
        return miniConsultationRepository.findById(id).map(miniConsultationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<MiniConsultationDTO> findAll() {
        return miniConsultationRepository.findAll().stream().map(miniConsultationMapper::toDto).collect(Collectors.toList());
    }

    public void delete(Long id) {
        miniConsultationRepository.deleteById(id);
    }

    /**
     * Chercher la mini-consultation liée à une surveillance sheet.
     */
    @Transactional(readOnly = true)
    public java.util.List<MiniConsultationDTO> findBySurveillanceSheet(Long surveillanceSheetId) {
        return miniConsultationRepository
            .findBySurveillanceSheet_Id(surveillanceSheetId)
            .stream()
            .map(miniConsultationMapper::toDto)
            .collect(java.util.stream.Collectors.toList());
    }
}
