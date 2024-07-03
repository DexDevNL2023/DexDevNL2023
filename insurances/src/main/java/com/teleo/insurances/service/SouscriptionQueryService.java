package com.teleo.insurances.service;

import com.teleo.insurances.domain.*; // for static metamodels
import com.teleo.insurances.domain.Souscription;
import com.teleo.insurances.repository.SouscriptionRepository;
import com.teleo.insurances.service.criteria.SouscriptionCriteria;
import com.teleo.insurances.service.dto.SouscriptionDTO;
import com.teleo.insurances.service.mapper.SouscriptionMapper;
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
 * Service for executing complex queries for {@link Souscription} entities in the database.
 * The main input is a {@link SouscriptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SouscriptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SouscriptionQueryService extends QueryService<Souscription> {

    private static final Logger log = LoggerFactory.getLogger(SouscriptionQueryService.class);

    private final SouscriptionRepository souscriptionRepository;

    private final SouscriptionMapper souscriptionMapper;

    public SouscriptionQueryService(SouscriptionRepository souscriptionRepository, SouscriptionMapper souscriptionMapper) {
        this.souscriptionRepository = souscriptionRepository;
        this.souscriptionMapper = souscriptionMapper;
    }

    /**
     * Return a {@link Page} of {@link SouscriptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SouscriptionDTO> findByCriteria(SouscriptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Souscription> specification = createSpecification(criteria);
        return souscriptionRepository.findAll(specification, page).map(souscriptionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SouscriptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Souscription> specification = createSpecification(criteria);
        return souscriptionRepository.count(specification);
    }

    /**
     * Function to convert {@link SouscriptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Souscription> createSpecification(SouscriptionCriteria criteria) {
        Specification<Souscription> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Souscription_.id));
            }
            if (criteria.getNumeroSouscription() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getNumeroSouscription(), Souscription_.numeroSouscription)
                );
            }
            if (criteria.getDateSouscription() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateSouscription(), Souscription_.dateSouscription));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Souscription_.status));
            }
            if (criteria.getCumulSinistres() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCumulSinistres(), Souscription_.cumulSinistres));
            }
            if (criteria.getFrequencePaiement() != null) {
                specification = specification.and(buildSpecification(criteria.getFrequencePaiement(), Souscription_.frequencePaiement));
            }
            if (criteria.getUtilisateurId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUtilisateurId(), Souscription_.utilisateurId));
            }
            if (criteria.getPolicesAssurancesId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getPolicesAssurancesId(),
                        root -> root.join(Souscription_.policesAssurances, JoinType.LEFT).get(PolicesAssurances_.id)
                    )
                );
            }
        }
        return specification;
    }
}
