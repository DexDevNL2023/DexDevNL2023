package com.teleo.insurances.repository;

import com.teleo.insurances.domain.PolicesAssurances;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PolicesAssurances entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PolicesAssurancesRepository extends JpaRepository<PolicesAssurances, Long> {}
