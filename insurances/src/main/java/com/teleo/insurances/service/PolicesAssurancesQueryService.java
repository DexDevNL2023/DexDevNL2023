package com.teleo.insurances.service;

import com.teleo.insurances.domain.*; // for static metamodels
import com.teleo.insurances.domain.PolicesAssurances;
import com.teleo.insurances.repository.PolicesAssurancesRepository;
import com.teleo.insurances.service.criteria.PolicesAssurancesCriteria;
import com.teleo.insurances.service.dto.PolicesAssurancesDTO;
import com.teleo.insurances.service.mapper.PolicesAssurancesMapper;
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
 * Service for executing complex queries for {@link PolicesAssurances} entities in the database.
 * The main input is a {@link PolicesAssurancesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PolicesAssurancesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PolicesAssurancesQueryService extends QueryService<PolicesAssurances> {

    private static final Logger log = LoggerFactory.getLogger(PolicesAssurancesQueryService.class);

    private final PolicesAssurancesRepository policesAssurancesRepository;

    private final PolicesAssurancesMapper policesAssurancesMapper;

    public PolicesAssurancesQueryService(
        PolicesAssurancesRepository policesAssurancesRepository,
        PolicesAssurancesMapper policesAssurancesMapper
    ) {
        this.policesAssurancesRepository = policesAssurancesRepository;
        this.policesAssurancesMapper = policesAssurancesMapper;
    }

    /**
     * Return a {@link Page} of {@link PolicesAssurancesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PolicesAssurancesDTO> findByCriteria(PolicesAssurancesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PolicesAssurances> specification = createSpecification(criteria);
        return policesAssurancesRepository.findAll(specification, page).map(policesAssurancesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PolicesAssurancesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PolicesAssurances> specification = createSpecification(criteria);
        return policesAssurancesRepository.count(specification);
    }

    /**
     * Function to convert {@link PolicesAssurancesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PolicesAssurances> createSpecification(PolicesAssurancesCriteria criteria) {
        Specification<PolicesAssurances> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PolicesAssurances_.id));
            }
            if (criteria.getNumeroPolice() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumeroPolice(), PolicesAssurances_.numeroPolice));
            }
            if (criteria.getDateEmission() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateEmission(), PolicesAssurances_.dateEmission));
            }
            if (criteria.getDateExpiration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateExpiration(), PolicesAssurances_.dateExpiration));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PolicesAssurances_.description));
            }
            if (criteria.getMontantAssure() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontantAssure(), PolicesAssurances_.montantAssure));
            }
            if (criteria.getPlafondAssure() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlafondAssure(), PolicesAssurances_.plafondAssure));
            }
            if (criteria.getAssuranceId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getAssuranceId(),
                        root -> root.join(PolicesAssurances_.assurance, JoinType.LEFT).get(Assurance_.id)
                    )
                );
            }
        }
        return specification;
    }
}
