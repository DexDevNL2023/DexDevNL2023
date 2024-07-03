package com.teleo.insurances.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AssuranceCriteriaTest {

    @Test
    void newAssuranceCriteriaHasAllFiltersNullTest() {
        var assuranceCriteria = new AssuranceCriteria();
        assertThat(assuranceCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void assuranceCriteriaFluentMethodsCreatesFiltersTest() {
        var assuranceCriteria = new AssuranceCriteria();

        setAllFilters(assuranceCriteria);

        assertThat(assuranceCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void assuranceCriteriaCopyCreatesNullFilterTest() {
        var assuranceCriteria = new AssuranceCriteria();
        var copy = assuranceCriteria.copy();

        assertThat(assuranceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(assuranceCriteria)
        );
    }

    @Test
    void assuranceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var assuranceCriteria = new AssuranceCriteria();
        setAllFilters(assuranceCriteria);

        var copy = assuranceCriteria.copy();

        assertThat(assuranceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(assuranceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var assuranceCriteria = new AssuranceCriteria();

        assertThat(assuranceCriteria).hasToString("AssuranceCriteria{}");
    }

    private static void setAllFilters(AssuranceCriteria assuranceCriteria) {
        assuranceCriteria.id();
        assuranceCriteria.nom();
        assuranceCriteria.description();
        assuranceCriteria.conditions();
        assuranceCriteria.type();
        assuranceCriteria.prixAssurance();
        assuranceCriteria.dateDebut();
        assuranceCriteria.dateFin();
        assuranceCriteria.garantieId();
        assuranceCriteria.distinct();
    }

    private static Condition<AssuranceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getConditions()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getPrixAssurance()) &&
                condition.apply(criteria.getDateDebut()) &&
                condition.apply(criteria.getDateFin()) &&
                condition.apply(criteria.getGarantieId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AssuranceCriteria> copyFiltersAre(AssuranceCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getConditions(), copy.getConditions()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getPrixAssurance(), copy.getPrixAssurance()) &&
                condition.apply(criteria.getDateDebut(), copy.getDateDebut()) &&
                condition.apply(criteria.getDateFin(), copy.getDateFin()) &&
                condition.apply(criteria.getGarantieId(), copy.getGarantieId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
