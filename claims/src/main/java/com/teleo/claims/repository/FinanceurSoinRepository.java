package com.teleo.claims.repository;

import com.teleo.claims.domain.FinanceurSoin;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FinanceurSoin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinanceurSoinRepository extends JpaRepository<FinanceurSoin, Long>, JpaSpecificationExecutor<FinanceurSoin> {}
