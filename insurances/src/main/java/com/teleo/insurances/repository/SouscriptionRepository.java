package com.teleo.insurances.repository;

import com.teleo.insurances.domain.Souscription;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Souscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SouscriptionRepository extends JpaRepository<Souscription, Long>, JpaSpecificationExecutor<Souscription> {}
