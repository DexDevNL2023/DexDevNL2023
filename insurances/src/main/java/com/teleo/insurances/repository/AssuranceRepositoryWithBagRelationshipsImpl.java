package com.teleo.insurances.repository;

import com.teleo.insurances.domain.Assurance;
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
public class AssuranceRepositoryWithBagRelationshipsImpl implements AssuranceRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ASSURANCES_PARAMETER = "assurances";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Assurance> fetchBagRelationships(Optional<Assurance> assurance) {
        return assurance.map(this::fetchGaranties);
    }

    @Override
    public Page<Assurance> fetchBagRelationships(Page<Assurance> assurances) {
        return new PageImpl<>(fetchBagRelationships(assurances.getContent()), assurances.getPageable(), assurances.getTotalElements());
    }

    @Override
    public List<Assurance> fetchBagRelationships(List<Assurance> assurances) {
        return Optional.of(assurances).map(this::fetchGaranties).orElse(Collections.emptyList());
    }

    Assurance fetchGaranties(Assurance result) {
        return entityManager
            .createQuery(
                "select assurance from Assurance assurance left join fetch assurance.garanties where assurance.id = :id",
                Assurance.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Assurance> fetchGaranties(List<Assurance> assurances) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, assurances.size()).forEach(index -> order.put(assurances.get(index).getId(), index));
        List<Assurance> result = entityManager
            .createQuery(
                "select assurance from Assurance assurance left join fetch assurance.garanties where assurance in :assurances",
                Assurance.class
            )
            .setParameter(ASSURANCES_PARAMETER, assurances)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
