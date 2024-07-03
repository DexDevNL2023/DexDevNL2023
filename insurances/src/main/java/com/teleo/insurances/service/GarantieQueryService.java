package com.teleo.insurances.service;

import com.teleo.insurances.domain.*; // for static metamodels
import com.teleo.insurances.domain.Garantie;
import com.teleo.insurances.repository.GarantieRepository;
import com.teleo.insurances.service.criteria.GarantieCriteria;
import com.teleo.insurances.service.dto.GarantieDTO;
import com.teleo.insurances.service.mapper.GarantieMapper;
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
 * Service for executing complex queries for {@link Garantie} entities in the database.
 * The main input is a {@link GarantieCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link GarantieDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GarantieQueryService extends QueryService<Garantie> {

    private static final Logger log = LoggerFactory.getLogger(GarantieQueryService.class);

    private final GarantieRepository garantieRepository;

    private final GarantieMapper garantieMapper;

    public GarantieQueryService(GarantieRepository garantieRepository, GarantieMapper garantieMapper) {
        this.garantieRepository = garantieRepository;
        this.garantieMapper = garantieMapper;
    }

    /**
     * Return a {@link Page} of {@link GarantieDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GarantieDTO> findByCriteria(GarantieCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Garantie> specification = createSpecification(criteria);
        return garantieRepository.findAll(specification, page).map(garantieMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GarantieCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Garantie> specification = createSpecification(criteria);
        return garantieRepository.count(specification);
    }

    /**
     * Function to convert {@link GarantieCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Garantie> createSpecification(GarantieCriteria criteria) {
        Specification<Garantie> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Garantie_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Garantie_.description));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), Garantie_.dateDebut));
            }
            if (criteria.getDateFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFin(), Garantie_.dateFin));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Garantie_.status));
            }
            if (criteria.getAssuranceId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAssuranceId(), root -> root.join(Garantie_.assurances, JoinType.LEFT).get(Assurance_.id))
                );
            }
        }
        return specification;
    }
}
