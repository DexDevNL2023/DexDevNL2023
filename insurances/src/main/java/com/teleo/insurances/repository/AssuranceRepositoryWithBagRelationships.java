package com.teleo.insurances.repository;

import com.teleo.insurances.domain.Assurance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AssuranceRepositoryWithBagRelationships {
    Optional<Assurance> fetchBagRelationships(Optional<Assurance> assurance);

    List<Assurance> fetchBagRelationships(List<Assurance> assurances);

    Page<Assurance> fetchBagRelationships(Page<Assurance> assurances);
}
