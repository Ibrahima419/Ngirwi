package sn.ngirwi.medical.web.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ngirwi.medical.service.MiniConsultationService;
import sn.ngirwi.medical.service.dto.MiniConsultationDTO;
import sn.ngirwi.medical.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping("/api/mini-consultations")
public class MiniConsultationResource {

    private final MiniConsultationService miniConsultationService;

    public MiniConsultationResource(MiniConsultationService miniConsultationService) {
        this.miniConsultationService = miniConsultationService;
    }

    @PostMapping
    public ResponseEntity<MiniConsultationDTO> create(@Valid @RequestBody MiniConsultationDTO dto) {
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new miniConsultation cannot already have an ID", "miniConsultation", "idexists");
        }
        try {
            MiniConsultationDTO result = miniConsultationService.save(dto);
            return ResponseEntity.created(URI.create("/api/mini-consultations/" + result.getId())).body(result);
        } catch (IllegalStateException ex) {
            throw new BadRequestAlertException(ex.getMessage(), "miniConsultation", "business_rule_violation");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MiniConsultationDTO> update(@PathVariable Long id, @Valid @RequestBody MiniConsultationDTO dto) {
        if (dto.getId() == null || !dto.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        MiniConsultationDTO result = miniConsultationService.save(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MiniConsultationDTO> getOne(@PathVariable Long id) {
        Optional<MiniConsultationDTO> dto = miniConsultationService.findOne(id);
        return dto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<MiniConsultationDTO>> getAll() {
        return ResponseEntity.ok(miniConsultationService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        miniConsultationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-surveillance/{surveillanceSheetId}")
    public ResponseEntity<List<MiniConsultationDTO>> getBySurveillance(@PathVariable Long surveillanceSheetId) {
        return ResponseEntity.ok(miniConsultationService.findBySurveillanceSheet(surveillanceSheetId));
    }
}
