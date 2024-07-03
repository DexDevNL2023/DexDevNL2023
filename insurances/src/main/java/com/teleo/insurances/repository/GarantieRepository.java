package com.teleo.insurances.repository;

import com.teleo.insurances.domain.Garantie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Garantie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GarantieRepository extends JpaRepository<Garantie, Long>, JpaSpecificationExecutor<Garantie> {}
