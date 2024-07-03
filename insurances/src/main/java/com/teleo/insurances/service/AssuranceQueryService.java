package com.teleo.insurances.service;

import com.teleo.insurances.domain.*; // for static metamodels
import com.teleo.insurances.domain.Assurance;
import com.teleo.insurances.repository.AssuranceRepository;
import com.teleo.insurances.service.criteria.AssuranceCriteria;
import com.teleo.insurances.service.dto.AssuranceDTO;
import com.teleo.insurances.service.mapper.AssuranceMapper;
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
 * Service for executing complex queries for {@link Assurance} entities in the database.
 * The main input is a {@link AssuranceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AssuranceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssuranceQueryService extends QueryService<Assurance> {

    private static final Logger log = LoggerFactory.getLogger(AssuranceQueryService.class);

    private final AssuranceRepository assuranceRepository;

    private final AssuranceMapper assuranceMapper;

    public AssuranceQueryService(AssuranceRepository assuranceRepository, AssuranceMapper assuranceMapper) {
        this.assuranceRepository = assuranceRepository;
        this.assuranceMapper = assuranceMapper;
    }

    /**
     * Return a {@link Page} of {@link AssuranceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssuranceDTO> findByCriteria(AssuranceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Assurance> specification = createSpecification(criteria);
        return assuranceRepository.fetchBagRelationships(assuranceRepository.findAll(specification, page)).map(assuranceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssuranceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Assurance> specification = createSpecification(criteria);
        return assuranceRepository.count(specification);
    }

    /**
     * Function to convert {@link AssuranceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Assurance> createSpecification(AssuranceCriteria criteria) {
        Specification<Assurance> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Assurance_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Assurance_.nom));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Assurance_.description));
            }
            if (criteria.getConditions() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConditions(), Assurance_.conditions));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Assurance_.type));
            }
            if (criteria.getPrixAssurance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrixAssurance(), Assurance_.prixAssurance));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), Assurance_.dateDebut));
            }
            if (criteria.getDateFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFin(), Assurance_.dateFin));
            }
            if (criteria.getGarantieId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getGarantieId(), root -> root.join(Assurance_.garanties, JoinType.LEFT).get(Garantie_.id))
                );
            }
        }
        return specification;
    }
}
