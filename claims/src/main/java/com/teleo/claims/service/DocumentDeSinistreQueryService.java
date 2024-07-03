package com.teleo.claims.service;

import com.teleo.claims.domain.*; // for static metamodels
import com.teleo.claims.domain.DocumentDeSinistre;
import com.teleo.claims.repository.DocumentDeSinistreRepository;
import com.teleo.claims.service.criteria.DocumentDeSinistreCriteria;
import com.teleo.claims.service.dto.DocumentDeSinistreDTO;
import com.teleo.claims.service.mapper.DocumentDeSinistreMapper;
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
 * Service for executing complex queries for {@link DocumentDeSinistre} entities in the database.
 * The main input is a {@link DocumentDeSinistreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentDeSinistreDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentDeSinistreQueryService extends QueryService<DocumentDeSinistre> {

    private static final Logger log = LoggerFactory.getLogger(DocumentDeSinistreQueryService.class);

    private final DocumentDeSinistreRepository documentDeSinistreRepository;

    private final DocumentDeSinistreMapper documentDeSinistreMapper;

    public DocumentDeSinistreQueryService(
        DocumentDeSinistreRepository documentDeSinistreRepository,
        DocumentDeSinistreMapper documentDeSinistreMapper
    ) {
        this.documentDeSinistreRepository = documentDeSinistreRepository;
        this.documentDeSinistreMapper = documentDeSinistreMapper;
    }

    /**
     * Return a {@link Page} of {@link DocumentDeSinistreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentDeSinistreDTO> findByCriteria(DocumentDeSinistreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentDeSinistre> specification = createSpecification(criteria);
        return documentDeSinistreRepository.findAll(specification, page).map(documentDeSinistreMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentDeSinistreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocumentDeSinistre> specification = createSpecification(criteria);
        return documentDeSinistreRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentDeSinistreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentDeSinistre> createSpecification(DocumentDeSinistreCriteria criteria) {
        Specification<DocumentDeSinistre> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocumentDeSinistre_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), DocumentDeSinistre_.nom));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), DocumentDeSinistre_.type));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), DocumentDeSinistre_.url));
            }
            if (criteria.getDateTelechargement() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getDateTelechargement(), DocumentDeSinistre_.dateTelechargement)
                );
            }
            if (criteria.getSinistreId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getSinistreId(),
                        root -> root.join(DocumentDeSinistre_.sinistre, JoinType.LEFT).get(Sinistre_.id)
                    )
                );
            }
        }
        return specification;
    }
}
