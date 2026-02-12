package sn.ngirwi.medical.service;

import java.math.BigDecimal;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.Bill;
import sn.ngirwi.medical.domain.BillElement;
import sn.ngirwi.medical.domain.Patient;
import sn.ngirwi.medical.domain.User;
import sn.ngirwi.medical.repository.BillElementRepository;
import sn.ngirwi.medical.repository.BillRepository;
import sn.ngirwi.medical.repository.PatientRepository;
import sn.ngirwi.medical.repository.UserRepository;
import sn.ngirwi.medical.service.dto.BillDTO;
import sn.ngirwi.medical.service.dto.BillElementDTO;
import sn.ngirwi.medical.service.mapper.BillElementMapper;
import sn.ngirwi.medical.service.mapper.BillMapper;

/**
 * Service Implementation for managing {@link Bill}.
 */
@Service
@Transactional
public class BillService {

    private final Logger log = LoggerFactory.getLogger(BillService.class);

    private final BillRepository billRepository;

    private final BillElementRepository billElementRepository;

    private final UserRepository userRepository;

    private final BillMapper billMapper;

    private final BillElementMapper billElementMapper;
    private final CurrentHospitalProvider currentHospitalProvider;
    private final PatientRepository patientRepository;

    public BillService(
        BillRepository billRepository,
        BillElementRepository billElementRepository,
        UserRepository userRepository,
        BillMapper billMapper,
        BillElementMapper billElementMapper,
        CurrentHospitalProvider currentHospitalProvider,
        PatientRepository patientRepository
    ) {
        this.billRepository = billRepository;
        this.billElementRepository = billElementRepository;
        this.userRepository = userRepository;
        this.billMapper = billMapper;
        this.billElementMapper = billElementMapper;
        this.currentHospitalProvider = currentHospitalProvider;
        this.patientRepository = patientRepository;
    }

    /**
     * Save a bill.
     *
     * @param billDTO the entity to save.
     * @return the persisted entity.
     */
    public BillDTO save(BillDTO billDTO) {
        log.debug("Request to save Bill : {}", billDTO);
        Long patientId = billDTO.getPatient() != null ? billDTO.getPatient().getId() : null;
        if (patientId == null) {
            throw new IllegalArgumentException("patientId is required");
        }
        Patient p = patientRepository.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Patient not found id=" + patientId));
        assertSameHospital(p.getHospitalId());
        Bill bill = billMapper.toEntity(billDTO);
        bill = billRepository.save(bill);
        return billMapper.toDto(bill);
    }

    public Bill mapElements(BillDTO billDto, Bill bill) {
        Set<BillElement> billElements = new HashSet<>();
        for (BillElementDTO billElementDTO : billDto.getBillElements()) {
            BillElement billElement = billElementMapper.toEntity(billElementDTO);
            billElement.setBill(bill);

            billElements.add(billElement);
        }

        bill.setBillElements(billElements);
        return bill;
    }

    @Transactional
    public BillDTO saveBis(BillDTO billDTO) {
        log.debug("Request to save bill : {}", billDTO);
        Long patientId = billDTO.getPatient() != null ? billDTO.getPatient().getId() : null;
        if (patientId == null) {
            throw new IllegalArgumentException("patientId is required");
        }
        Patient p = patientRepository.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Patient not found id=" + patientId));
        assertSameHospital(p.getHospitalId());

        Bill bill = billMapper.toEntity(billDTO);
        bill = mapElements(billDTO, bill);

        // Save the Bill entity
        if (bill.getTotal() == null) {
            bill.setTotal(calculateTotal(bill));
        }
        bill = billRepository.save(bill);

        // Ensure bill.getBillElements() is not null and iterate over elements
        if (bill.getBillElements() != null) {
            for (BillElement element : bill.getBillElements()) {
                element.setBill(bill);
                element = billElementRepository.save(element);
                log.debug("Saved BillElement: {}", element);
            }
        } else {
            log.debug("No BillElements found in bill");
        }

        log.debug("Saved Bill entity: {}", bill);

        billDTO.setId(bill.getId());

        return billDTO;
    }

    /**
     * Calcule le total de la facture à partir des éléments en mémoire.
     * 
     * IMPORTANT: On utilise toujours le calcul en mémoire plutôt que la requête native
     * pour éviter les problèmes de timing transactionnel (la requête native pourrait
     * ne pas voir les éléments tout juste sauvegardés dans la même transaction).
     * 
     * Formule: Σ(prix × quantité × (1 - remise/100))
     */
    public BigDecimal calculateTotal(Bill bill) {
        BigDecimal total = BigDecimal.ZERO;
        if (bill.getBillElements() == null || bill.getBillElements().isEmpty()) {
            log.debug("No BillElements found in bill");
            return total;
        }
        for (BillElement element : bill.getBillElements()) {
            BigDecimal unit = element.getPrice() == null ? BigDecimal.ZERO : BigDecimal.valueOf(element.getPrice());
            int qty = element.getQuantity() == null ? 0 : element.getQuantity();
            BigDecimal pct = element.getPercentage() == null ? BigDecimal.ZERO : BigDecimal.valueOf(element.getPercentage());
            BigDecimal gross = unit.multiply(BigDecimal.valueOf(qty));
            BigDecimal net = gross.subtract(gross.multiply(pct).divide(BigDecimal.valueOf(100), 6, java.math.RoundingMode.HALF_UP));
            total = total.add(net);
        }
        return total.setScale(0, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Update a bill.
     *
     * @param billDTO the entity to save.
     * @return the persisted entity.
     */
    public BillDTO update(BillDTO billDTO) {
        log.debug("Request to update Bill : {}", billDTO);
        if (billDTO.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }
        billRepository
            .findById(billDTO.getId())
            .ifPresent(existing -> assertSameHospital(existing.getPatient() != null ? existing.getPatient().getHospitalId() : null));
        Bill bill = billMapper.toEntity(billDTO);
        bill = billRepository.save(bill);
        return billMapper.toDto(bill);
    }

    public BillDTO updateBis(BillDTO billDTO) {
        log.debug("Request to update bill : {}", billDTO);
        if (billDTO.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }
        billRepository
            .findById(billDTO.getId())
            .ifPresent(existing -> assertSameHospital(existing.getPatient() != null ? existing.getPatient().getHospitalId() : null));

        // Get existing elements from DB
        List<BillElement> existingElements = billElementRepository.findByBill_Id(billDTO.getId());
        
        // Map the bill and its elements (preserves IDs from DTO)
        Bill bill = billMapper.toEntity(billDTO);
        bill = mapElements(billDTO, bill);
        
        // Collect IDs of submitted elements (for deletion check)
        Set<Long> submittedIds = new HashSet<>();
        if (bill.getBillElements() != null) {
            for (BillElement el : bill.getBillElements()) {
                if (el.getId() != null) {
                    submittedIds.add(el.getId());
                }
            }
        }
        
        // Smart update: DELETE elements that were removed by user
        for (BillElement existing : existingElements) {
            if (!submittedIds.contains(existing.getId())) {
                log.debug("Deleting removed bill element: {}", existing.getId());
                billElementRepository.deleteById(existing.getId());
            }
        }

        // Save bill first
        bill = billRepository.save(bill);

        // Smart update: UPDATE existing or CREATE new elements
        if (bill.getBillElements() != null) {
            for (BillElement element : bill.getBillElements()) {
                element.setBill(bill);
                if (element.getId() != null) {
                    log.debug("Updating existing bill element: {}", element.getId());
                } else {
                    log.debug("Creating new bill element: {}", element.getName());
                }
                billElementRepository.save(element);
            }
        }
        
        // Recalculate total after all elements are saved
        bill.setTotal(calculateTotal(bill));
        bill = billRepository.save(bill);

        log.debug(bill.toString());

        billDTO.setId(bill.getId());

        return billDTO;
    }

    /**
     * Partially update a bill.
     *
     * @param billDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BillDTO> partialUpdate(BillDTO billDTO) {
        log.debug("Request to partially update Bill : {}", billDTO);

        return billRepository
            .findById(billDTO.getId())
            .map(existingBill -> {
                assertSameHospital(existingBill.getPatient() != null ? existingBill.getPatient().getHospitalId() : null);
                billMapper.partialUpdate(existingBill, billDTO);

                return existingBill;
            })
            .map(billRepository::save)
            .map(billMapper::toDto);
    }

    /**
     * Get all the bills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    //public Page<BillDTO> findAll(Pageable pageable) {
    //   log.debug("Request to get all Bills");
    // return billRepository.findAll(pageable).map(billMapper::toDto);
    //}
    public Page<Bill> findAll(Pageable pageable) {
        log.debug("Request to get all Bills");
        return currentHospitalProvider
            .getCurrentHospitalId()
            .map(hid -> billRepository.findByPatient_HospitalId(hid, pageable))
            .orElseGet(() -> billRepository.findAll(pageable));
    }

    @Transactional(readOnly = true)
    public Page<Bill> findAll(Pageable pageable, Long id) {
        log.debug("Request to get all bills by hospital " + id);
        List<User> users = userRepository.findByHospitalId(id);
        List<String> logins = new ArrayList<>();
        if (users.size() > 0) {
            for (User user : users) {
                log.debug(user.toString());
                logins.add(user.getLogin());
            }
        }
        return billRepository.findByAuthorIn(logins, pageable);
        //return billRepository.findAll(pageable);
    }

    /**
     * Get one bill by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Bill> findOne(Long id) {
        log.debug("Request to get Bill : {}", id);
        return currentHospitalProvider
            .getCurrentHospitalId()
            .map(hid -> billRepository.findByIdAndPatient_HospitalId(id, hid))
            .orElseGet(() -> billRepository.findById(id));
    }

    /**
     * Delete the bill by id.
     * cascade = ALL + orphanRemoval = true on Bill.billElements ensures
     * that deleting the Bill automatically deletes all its BillElements.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Bill : {}", id);
        billRepository
            .findById(id)
            .ifPresent(existing -> assertSameHospital(existing.getPatient() != null ? existing.getPatient().getHospitalId() : null));
        billRepository.deleteById(id);
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
