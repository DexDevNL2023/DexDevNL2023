package com.teleo.accounts.repository;

import com.teleo.accounts.domain.MedicalDocument;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MedicalDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicalDocumentRepository extends JpaRepository<MedicalDocument, Long> {}
