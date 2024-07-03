package com.teleo.claims.repository;

import com.teleo.claims.domain.PrestationSoin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PrestationSoinRepositoryWithBagRelationshipsImpl implements PrestationSoinRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PRESTATIONSOINS_PARAMETER = "prestationSoins";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PrestationSoin> fetchBagRelationships(Optional<PrestationSoin> prestationSoin) {
        return prestationSoin.map(this::fetchFinanceurSoins);
    }

    @Override
    public Page<PrestationSoin> fetchBagRelationships(Page<PrestationSoin> prestationSoins) {
        return new PageImpl<>(
            fetchBagRelationships(prestationSoins.getContent()),
            prestationSoins.getPageable(),
            prestationSoins.getTotalElements()
        );
    }

    @Override
    public List<PrestationSoin> fetchBagRelationships(List<PrestationSoin> prestationSoins) {
        return Optional.of(prestationSoins).map(this::fetchFinanceurSoins).orElse(Collections.emptyList());
    }

    PrestationSoin fetchFinanceurSoins(PrestationSoin result) {
        return entityManager
            .createQuery(
                "select prestationSoin from PrestationSoin prestationSoin left join fetch prestationSoin.financeurSoins where prestationSoin.id = :id",
                PrestationSoin.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<PrestationSoin> fetchFinanceurSoins(List<PrestationSoin> prestationSoins) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, prestationSoins.size()).forEach(index -> order.put(prestationSoins.get(index).getId(), index));
        List<PrestationSoin> result = entityManager
            .createQuery(
                "select prestationSoin from PrestationSoin prestationSoin left join fetch prestationSoin.financeurSoins where prestationSoin in :prestationSoins",
                PrestationSoin.class
            )
            .setParameter(PRESTATIONSOINS_PARAMETER, prestationSoins)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
