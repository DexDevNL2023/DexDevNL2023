package com.teleo.insurances.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaiementCriteriaTest {

    @Test
    void newPaiementCriteriaHasAllFiltersNullTest() {
        var paiementCriteria = new PaiementCriteria();
        assertThat(paiementCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void paiementCriteriaFluentMethodsCreatesFiltersTest() {
        var paiementCriteria = new PaiementCriteria();

        setAllFilters(paiementCriteria);

        assertThat(paiementCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void paiementCriteriaCopyCreatesNullFilterTest() {
        var paiementCriteria = new PaiementCriteria();
        var copy = paiementCriteria.copy();

        assertThat(paiementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(paiementCriteria)
        );
    }

    @Test
    void paiementCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paiementCriteria = new PaiementCriteria();
        setAllFilters(paiementCriteria);

        var copy = paiementCriteria.copy();

        assertThat(paiementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(paiementCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paiementCriteria = new PaiementCriteria();

        assertThat(paiementCriteria).hasToString("PaiementCriteria{}");
    }

    private static void setAllFilters(PaiementCriteria paiementCriteria) {
        paiementCriteria.id();
        paiementCriteria.datePaiement();
        paiementCriteria.montant();
        paiementCriteria.type();
        paiementCriteria.status();
        paiementCriteria.souscriptionId();
        paiementCriteria.distinct();
    }

    private static Condition<PaiementCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDatePaiement()) &&
                condition.apply(criteria.getMontant()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getSouscriptionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PaiementCriteria> copyFiltersAre(PaiementCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDatePaiement(), copy.getDatePaiement()) &&
                condition.apply(criteria.getMontant(), copy.getMontant()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getSouscriptionId(), copy.getSouscriptionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
