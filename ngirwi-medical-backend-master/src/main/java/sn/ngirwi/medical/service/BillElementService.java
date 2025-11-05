package sn.ngirwi.medical.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.BillElement;
import sn.ngirwi.medical.repository.BillElementRepository;
import sn.ngirwi.medical.service.dto.BillElementDTO;
import sn.ngirwi.medical.service.dto.MedecineDTO;
import sn.ngirwi.medical.service.mapper.BillElementMapper;

/**
 * Service Implementation for managing {@link BillElement}.
 */
@Service
@Transactional
public class BillElementService {

    private final Logger log = LoggerFactory.getLogger(BillElementService.class);

    private final BillElementRepository billElementRepository;

    private final BillElementMapper billElementMapper;

    public BillElementService(BillElementRepository billElementRepository, BillElementMapper billElementMapper) {
        this.billElementRepository = billElementRepository;
        this.billElementMapper = billElementMapper;
    }

    /**
     * Save a billElement.
     *
     * @param billElementDTO the entity to save.
     * @return the persisted entity.
     */
    public BillElementDTO save(BillElementDTO billElementDTO) {
        log.debug("Request to save BillElement : {}", billElementDTO);
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
        return billElementRepository.findAll(pageable).map(billElementMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<BillElementDTO> findAll(Long id) {
        log.debug("Request to get all BillElemnts");
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
        return billElementRepository.findById(id).map(billElementMapper::toDto);
    }

    /**
     * Delete the billElement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BillElement : {}", id);
        billElementRepository.deleteById(id);
    }
}
