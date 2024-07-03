package com.teleo.claims.repository;

import com.teleo.claims.domain.PrestationSoin;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PrestationSoin entity.
 *
 * When extending this class, extend PrestationSoinRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface PrestationSoinRepository extends PrestationSoinRepositoryWithBagRelationships, JpaRepository<PrestationSoin, Long> {
    default Optional<PrestationSoin> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<PrestationSoin> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<PrestationSoin> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
