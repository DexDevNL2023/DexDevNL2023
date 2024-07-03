package com.teleo.claims.service;

import com.teleo.claims.domain.*; // for static metamodels
import com.teleo.claims.domain.Sinistre;
import com.teleo.claims.repository.SinistreRepository;
import com.teleo.claims.service.criteria.SinistreCriteria;
import com.teleo.claims.service.dto.SinistreDTO;
import com.teleo.claims.service.mapper.SinistreMapper;
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
 * Service for executing complex queries for {@link Sinistre} entities in the database.
 * The main input is a {@link SinistreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SinistreDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SinistreQueryService extends QueryService<Sinistre> {

    private static final Logger log = LoggerFactory.getLogger(SinistreQueryService.class);

    private final SinistreRepository sinistreRepository;

    private final SinistreMapper sinistreMapper;

    public SinistreQueryService(SinistreRepository sinistreRepository, SinistreMapper sinistreMapper) {
        this.sinistreRepository = sinistreRepository;
        this.sinistreMapper = sinistreMapper;
    }

    /**
     * Return a {@link Page} of {@link SinistreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SinistreDTO> findByCriteria(SinistreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sinistre> specification = createSpecification(criteria);
        return sinistreRepository.findAll(specification, page).map(sinistreMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SinistreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sinistre> specification = createSpecification(criteria);
        return sinistreRepository.count(specification);
    }

    /**
     * Function to convert {@link SinistreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sinistre> createSpecification(SinistreCriteria criteria) {
        Specification<Sinistre> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sinistre_.id));
            }
            if (criteria.getNumeroSinistre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumeroSinistre(), Sinistre_.numeroSinistre));
            }
            if (criteria.getDateDeclaration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDeclaration(), Sinistre_.dateDeclaration));
            }
            if (criteria.getDateTraitement() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTraitement(), Sinistre_.dateTraitement));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Sinistre_.status));
            }
            if (criteria.getRaison() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRaison(), Sinistre_.raison));
            }
            if (criteria.getMontantReclame() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontantReclame(), Sinistre_.montantReclame));
            }
            if (criteria.getMontantApprouve() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontantApprouve(), Sinistre_.montantApprouve));
            }
            if (criteria.getSouscriptionId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSouscriptionId(), Sinistre_.souscriptionId));
            }
            if (criteria.getFournisseurId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getFournisseurId(),
                        root -> root.join(Sinistre_.fournisseur, JoinType.LEFT).get(Fournisseur_.id)
                    )
                );
            }
            if (criteria.getPrestationsSoinId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getPrestationsSoinId(),
                        root -> root.join(Sinistre_.prestationsSoin, JoinType.LEFT).get(PrestationSoin_.id)
                    )
                );
            }
        }
        return specification;
    }
}
