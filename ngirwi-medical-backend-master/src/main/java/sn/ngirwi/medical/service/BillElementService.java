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
import sn.ngirwi.medical.domain.BillElement;
import sn.ngirwi.medical.repository.BillElementRepository;
import sn.ngirwi.medical.repository.BillRepository;
import sn.ngirwi.medical.service.dto.BillElementDTO;
import sn.ngirwi.medical.service.dto.MedecineDTO;
import sn.ngirwi.medical.service.mapper.BillElementMapper;
import sn.ngirwi.medical.service.CurrentHospitalProvider;

/**
 * Service Implementation for managing {@link BillElement}.
 */
@Service
@Transactional
public class BillElementService {

    private final Logger log = LoggerFactory.getLogger(BillElementService.class);

    private final BillElementRepository billElementRepository;

    private final BillElementMapper billElementMapper;
    private final CurrentHospitalProvider currentHospitalProvider;
    private final BillRepository billRepository;

    public BillElementService(
        BillElementRepository billElementRepository,
        BillElementMapper billElementMapper,
        CurrentHospitalProvider currentHospitalProvider,
        BillRepository billRepository
    ) {
        this.billElementRepository = billElementRepository;
        this.billElementMapper = billElementMapper;
        this.currentHospitalProvider = currentHospitalProvider;
        this.billRepository = billRepository;
    }

    /**
     * Save a billElement.
     *
     * @param billElementDTO the entity to save.
     * @return the persisted entity.
     */
    public BillElementDTO save(BillElementDTO billElementDTO) {
        log.debug("Request to save BillElement : {}", billElementDTO);
        Long billId = billElementDTO.getBill() != null ? billElementDTO.getBill().getId() : null;
        if (billId == null) {
            throw new IllegalArgumentException("billId is required");
        }
        billRepository
            .findById(billId)
            .ifPresent(b -> assertSameHospital(b.getPatient() != null ? b.getPatient().getHospitalId() : null));
        BillElement billElement = billElementMapper.toEntity(billElementDTO);
        billElement = billElementRepository.save(billElement);
        return billElementMapper.toDto(billElement);
    }

    /**
     * Update a billElement.
     *
     * @param billElementDTO the entity to save.
     * @return the persisted entity.
     */
    public BillElementDTO update(BillElementDTO billElementDTO) {
        log.debug("Request to update BillElement : {}", billElementDTO);
        if (billElementDTO.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }
        billElementRepository
            .findById(billElementDTO.getId())
            .ifPresent(existing -> assertSameHospital(existing.getBill() != null && existing.getBill().getPatient() != null ? existing.getBill().getPatient().getHospitalId() : null));
        BillElement billElement = billElementMapper.toEntity(billElementDTO);
        billElement = billElementRepository.save(billElement);
        return billElementMapper.toDto(billElement);
    }

    /**
     * Partially update a billElement.
     *
     * @param billElementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BillElementDTO> partialUpdate(BillElementDTO billElementDTO) {
        log.debug("Request to partially update BillElement : {}", billElementDTO);

        return billElementRepository
            .findById(billElementDTO.getId())
            .map(existingBillElement -> {
                assertSameHospital(existingBillElement.getBill() != null && existingBillElement.getBill().getPatient() != null ? existingBillElement.getBill().getPatient().getHospitalId() : null);
                billElementMapper.partialUpdate(existingBillElement, billElementDTO);

                return existingBillElement;
            })
            .map(billElementRepository::save)
            .map(billElementMapper::toDto);
    }

    /**
     * Get all the billElements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BillElementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BillElements");
        return currentHospitalProvider
            .getCurrentHospitalId()
            .map(hid -> billElementRepository.findByBill_Patient_HospitalId(hid, pageable))
            .orElseGet(() -> billElementRepository.findAll(pageable))
            .map(billElementMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<BillElementDTO> findAll(Long id) {
        log.debug("Request to get all BillElemnts");
        billRepository
            .findById(id)
            .ifPresent(b -> assertSameHospital(b.getPatient() != null ? b.getPatient().getHospitalId() : null));
        return billElementMapper.toDto(billElementRepository.findByBill_Id(id));
    }

    /**
     * Get one billElement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BillElementDTO> findOne(Long id) {
        log.debug("Request to get BillElement : {}", id);
        return currentHospitalProvider
            .getCurrentHospitalId()
            .map(hid -> billElementRepository.findByIdAndBill_Patient_HospitalId(id, hid))
            .orElseGet(() -> billElementRepository.findById(id))
            .map(billElementMapper::toDto);
    }

    /**
     * Delete the billElement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BillElement : {}", id);
        billElementRepository
            .findById(id)
            .ifPresent(existing -> assertSameHospital(existing.getBill() != null && existing.getBill().getPatient() != null ? existing.getBill().getPatient().getHospitalId() : null));
        billElementRepository.deleteById(id);
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
}
