package com.teleo.accounts.repository;

import com.teleo.accounts.domain.MedicalRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MedicalRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {}
