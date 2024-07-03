package com.teleo.insurances.service;

import com.teleo.insurances.domain.*; // for static metamodels
import com.teleo.insurances.domain.Paiement;
import com.teleo.insurances.repository.PaiementRepository;
import com.teleo.insurances.service.criteria.PaiementCriteria;
import com.teleo.insurances.service.dto.PaiementDTO;
import com.teleo.insurances.service.mapper.PaiementMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Paiement} entities in the database.
 * The main input is a {@link PaiementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PaiementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaiementQueryService extends QueryService<Paiement> {

    private static final Logger log = LoggerFactory.getLogger(PaiementQueryService.class);

    private final PaiementRepository paiementRepository;

    private final PaiementMapper paiementMapper;

    public PaiementQueryService(PaiementRepository paiementRepository, PaiementMapper paiementMapper) {
        this.paiementRepository = paiementRepository;
        this.paiementMapper = paiementMapper;
    }

    /**
     * Return a {@link Page} of {@link PaiementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaiementDTO> findByCriteria(PaiementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Paiement> specification = createSpecification(criteria);
        return paiementRepository.findAll(specification, page).map(paiementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaiementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Paiement> specification = createSpecification(criteria);
        return paiementRepository.count(specification);
    }

    /**
     * Function to convert {@link PaiementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Paiement> createSpecification(PaiementCriteria criteria) {
        Specification<Paiement> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Paiement_.id));
            }
            if (criteria.getDatePaiement() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatePaiement(), Paiement_.datePaiement));
            }
            if (criteria.getMontant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontant(), Paiement_.montant));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Paiement_.type));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Paiement_.status));
            }
            if (criteria.getSouscriptionId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getSouscriptionId(),
                        root -> root.join(Paiement_.souscription, JoinType.LEFT).get(Souscription_.id)
                    )
                );
            }
        }
        return specification;
    }
}
