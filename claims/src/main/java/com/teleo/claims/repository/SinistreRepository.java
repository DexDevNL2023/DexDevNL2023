package com.teleo.claims.repository;

import com.teleo.claims.domain.Sinistre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sinistre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SinistreRepository extends JpaRepository<Sinistre, Long> {}
