package com.teleo.insurances.service;

import com.teleo.insurances.domain.*; // for static metamodels
import com.teleo.insurances.domain.Rapport;
import com.teleo.insurances.repository.RapportRepository;
import com.teleo.insurances.service.criteria.RapportCriteria;
import com.teleo.insurances.service.dto.RapportDTO;
import com.teleo.insurances.service.mapper.RapportMapper;
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
 * Service for executing complex queries for {@link Rapport} entities in the database.
 * The main input is a {@link RapportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RapportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RapportQueryService extends QueryService<Rapport> {

    private static final Logger log = LoggerFactory.getLogger(RapportQueryService.class);

    private final RapportRepository rapportRepository;

    private final RapportMapper rapportMapper;

    public RapportQueryService(RapportRepository rapportRepository, RapportMapper rapportMapper) {
        this.rapportRepository = rapportRepository;
        this.rapportMapper = rapportMapper;
    }

    /**
     * Return a {@link Page} of {@link RapportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RapportDTO> findByCriteria(RapportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Rapport> specification = createSpecification(criteria);
        return rapportRepository.findAll(specification, page).map(rapportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RapportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Rapport> specification = createSpecification(criteria);
        return rapportRepository.count(specification);
    }

    /**
     * Function to convert {@link RapportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Rapport> createSpecification(RapportCriteria criteria) {
        Specification<Rapport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Rapport_.id));
            }
            if (criteria.getTitre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitre(), Rapport_.titre));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Rapport_.description));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Rapport_.type));
            }
            if (criteria.getDateGeneration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateGeneration(), Rapport_.dateGeneration));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Rapport_.url));
            }
            if (criteria.getAssuranceId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAssuranceId(), root -> root.join(Rapport_.assurance, JoinType.LEFT).get(Assurance_.id))
                );
            }
        }
        return specification;
    }
}
