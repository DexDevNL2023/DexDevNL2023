package com.teleo.claims.repository;

import com.teleo.claims.domain.Reclamation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reclamation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long>, JpaSpecificationExecutor<Reclamation> {}
