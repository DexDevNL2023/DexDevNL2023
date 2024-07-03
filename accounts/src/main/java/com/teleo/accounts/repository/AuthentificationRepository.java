package com.teleo.accounts.repository;

import com.teleo.accounts.domain.Authentification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Authentification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthentificationRepository extends JpaRepository<Authentification, Long> {}
