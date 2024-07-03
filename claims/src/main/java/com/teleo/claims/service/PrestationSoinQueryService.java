package com.teleo.claims.service;

import com.teleo.claims.domain.*; // for static metamodels
import com.teleo.claims.domain.PrestationSoin;
import com.teleo.claims.repository.PrestationSoinRepository;
import com.teleo.claims.service.criteria.PrestationSoinCriteria;
import com.teleo.claims.service.dto.PrestationSoinDTO;
import com.teleo.claims.service.mapper.PrestationSoinMapper;
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
 * Service for executing complex queries for {@link PrestationSoin} entities in the database.
 * The main input is a {@link PrestationSoinCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PrestationSoinDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrestationSoinQueryService extends QueryService<PrestationSoin> {

    private static final Logger log = LoggerFactory.getLogger(PrestationSoinQueryService.class);

    private final PrestationSoinRepository prestationSoinRepository;

    private final PrestationSoinMapper prestationSoinMapper;

    public PrestationSoinQueryService(PrestationSoinRepository prestationSoinRepository, PrestationSoinMapper prestationSoinMapper) {
        this.prestationSoinRepository = prestationSoinRepository;
        this.prestationSoinMapper = prestationSoinMapper;
    }

    /**
     * Return a {@link Page} of {@link PrestationSoinDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrestationSoinDTO> findByCriteria(PrestationSoinCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PrestationSoin> specification = createSpecification(criteria);
        return prestationSoinRepository
            .fetchBagRelationships(prestationSoinRepository.findAll(specification, page))
            .map(prestationSoinMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrestationSoinCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PrestationSoin> specification = createSpecification(criteria);
        return prestationSoinRepository.count(specification);
    }

    /**
     * Function to convert {@link PrestationSoinCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PrestationSoin> createSpecification(PrestationSoinCriteria criteria) {
        Specification<PrestationSoin> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PrestationSoin_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), PrestationSoin_.nom));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PrestationSoin_.description));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), PrestationSoin_.type));
            }
            if (criteria.getMontant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontant(), PrestationSoin_.montant));
            }
            if (criteria.getFinanceurSoinId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getFinanceurSoinId(),
                        root -> root.join(PrestationSoin_.financeurSoin, JoinType.LEFT).get(FinanceurSoin_.id)
                    )
                );
            }
            if (criteria.getFinanceurSoinId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getFinanceurSoinId(),
                        root -> root.join(PrestationSoin_.financeurSoins, JoinType.LEFT).get(FinanceurSoin_.id)
                    )
                );
            }
        }
        return specification;
    }
}
