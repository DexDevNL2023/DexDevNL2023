package com.teleo.insurances.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SouscriptionCriteriaTest {

    @Test
    void newSouscriptionCriteriaHasAllFiltersNullTest() {
        var souscriptionCriteria = new SouscriptionCriteria();
        assertThat(souscriptionCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void souscriptionCriteriaFluentMethodsCreatesFiltersTest() {
        var souscriptionCriteria = new SouscriptionCriteria();

        setAllFilters(souscriptionCriteria);

        assertThat(souscriptionCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void souscriptionCriteriaCopyCreatesNullFilterTest() {
        var souscriptionCriteria = new SouscriptionCriteria();
        var copy = souscriptionCriteria.copy();

        assertThat(souscriptionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(souscriptionCriteria)
        );
    }

    @Test
    void souscriptionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var souscriptionCriteria = new SouscriptionCriteria();
        setAllFilters(souscriptionCriteria);

        var copy = souscriptionCriteria.copy();

        assertThat(souscriptionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(souscriptionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var souscriptionCriteria = new SouscriptionCriteria();

        assertThat(souscriptionCriteria).hasToString("SouscriptionCriteria{}");
    }

    private static void setAllFilters(SouscriptionCriteria souscriptionCriteria) {
        souscriptionCriteria.id();
        souscriptionCriteria.numeroSouscription();
        souscriptionCriteria.dateSouscription();
        souscriptionCriteria.status();
        souscriptionCriteria.cumulSinistres();
        souscriptionCriteria.frequencePaiement();
        souscriptionCriteria.utilisateurId();
        souscriptionCriteria.policesAssurancesId();
        souscriptionCriteria.distinct();
    }

    private static Condition<SouscriptionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumeroSouscription()) &&
                condition.apply(criteria.getDateSouscription()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCumulSinistres()) &&
                condition.apply(criteria.getFrequencePaiement()) &&
                condition.apply(criteria.getUtilisateurId()) &&
                condition.apply(criteria.getPolicesAssurancesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SouscriptionCriteria> copyFiltersAre(
        SouscriptionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumeroSouscription(), copy.getNumeroSouscription()) &&
                condition.apply(criteria.getDateSouscription(), copy.getDateSouscription()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCumulSinistres(), copy.getCumulSinistres()) &&
                condition.apply(criteria.getFrequencePaiement(), copy.getFrequencePaiement()) &&
                condition.apply(criteria.getUtilisateurId(), copy.getUtilisateurId()) &&
                condition.apply(criteria.getPolicesAssurancesId(), copy.getPolicesAssurancesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
