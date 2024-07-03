package com.teleo.claims.repository;

import com.teleo.claims.domain.PrestationSoin;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PrestationSoinRepositoryWithBagRelationships {
    Optional<PrestationSoin> fetchBagRelationships(Optional<PrestationSoin> prestationSoin);

    List<PrestationSoin> fetchBagRelationships(List<PrestationSoin> prestationSoins);

    Page<PrestationSoin> fetchBagRelationships(Page<PrestationSoin> prestationSoins);
}
