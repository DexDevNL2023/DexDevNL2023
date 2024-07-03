package com.teleo.claims.repository;

import com.teleo.claims.domain.DocumentDeSinistre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentDeSinistre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentDeSinistreRepository
    extends JpaRepository<DocumentDeSinistre, Long>, JpaSpecificationExecutor<DocumentDeSinistre> {}
