package com.teleo.claims.service;

import com.teleo.claims.domain.*; // for static metamodels
import com.teleo.claims.domain.FinanceurSoin;
import com.teleo.claims.repository.FinanceurSoinRepository;
import com.teleo.claims.service.criteria.FinanceurSoinCriteria;
import com.teleo.claims.service.dto.FinanceurSoinDTO;
import com.teleo.claims.service.mapper.FinanceurSoinMapper;
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
 * Service for executing complex queries for {@link FinanceurSoin} entities in the database.
 * The main input is a {@link FinanceurSoinCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FinanceurSoinDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FinanceurSoinQueryService extends QueryService<FinanceurSoin> {

    private static final Logger log = LoggerFactory.getLogger(FinanceurSoinQueryService.class);

    private final FinanceurSoinRepository financeurSoinRepository;

    private final FinanceurSoinMapper financeurSoinMapper;

    public FinanceurSoinQueryService(FinanceurSoinRepository financeurSoinRepository, FinanceurSoinMapper financeurSoinMapper) {
        this.financeurSoinRepository = financeurSoinRepository;
        this.financeurSoinMapper = financeurSoinMapper;
    }

    /**
     * Return a {@link Page} of {@link FinanceurSoinDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FinanceurSoinDTO> findByCriteria(FinanceurSoinCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FinanceurSoin> specification = createSpecification(criteria);
        return financeurSoinRepository.findAll(specification, page).map(financeurSoinMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FinanceurSoinCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FinanceurSoin> specification = createSpecification(criteria);
        return financeurSoinRepository.count(specification);
    }

    /**
     * Function to convert {@link FinanceurSoinCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FinanceurSoin> createSpecification(FinanceurSoinCriteria criteria) {
        Specification<FinanceurSoin> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FinanceurSoin_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), FinanceurSoin_.nom));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), FinanceurSoin_.type));
            }
            if (criteria.getAdresse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdresse(), FinanceurSoin_.adresse));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), FinanceurSoin_.telephone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), FinanceurSoin_.email));
            }
            if (criteria.getServicesFournis() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServicesFournis(), FinanceurSoin_.servicesFournis));
            }
            if (criteria.getPrestationSoinId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getPrestationSoinId(),
                        root -> root.join(FinanceurSoin_.prestationSoins, JoinType.LEFT).get(PrestationSoin_.id)
                    )
                );
            }
        }
        return specification;
    }
}
