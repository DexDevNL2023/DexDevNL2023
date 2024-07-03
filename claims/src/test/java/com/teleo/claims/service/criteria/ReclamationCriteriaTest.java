package com.teleo.claims.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReclamationCriteriaTest {

    @Test
    void newReclamationCriteriaHasAllFiltersNullTest() {
        var reclamationCriteria = new ReclamationCriteria();
        assertThat(reclamationCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void reclamationCriteriaFluentMethodsCreatesFiltersTest() {
        var reclamationCriteria = new ReclamationCriteria();

        setAllFilters(reclamationCriteria);

        assertThat(reclamationCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void reclamationCriteriaCopyCreatesNullFilterTest() {
        var reclamationCriteria = new ReclamationCriteria();
        var copy = reclamationCriteria.copy();

        assertThat(reclamationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(reclamationCriteria)
        );
    }

    @Test
    void reclamationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reclamationCriteria = new ReclamationCriteria();
        setAllFilters(reclamationCriteria);

        var copy = reclamationCriteria.copy();

        assertThat(reclamationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(reclamationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reclamationCriteria = new ReclamationCriteria();

        assertThat(reclamationCriteria).hasToString("ReclamationCriteria{}");
    }

    private static void setAllFilters(ReclamationCriteria reclamationCriteria) {
        reclamationCriteria.id();
        reclamationCriteria.numeroReclamation();
        reclamationCriteria.dateReclamation();
        reclamationCriteria.description();
        reclamationCriteria.status();
        reclamationCriteria.sinistreId();
        reclamationCriteria.distinct();
    }

    private static Condition<ReclamationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumeroReclamation()) &&
                condition.apply(criteria.getDateReclamation()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getSinistreId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReclamationCriteria> copyFiltersAre(ReclamationCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumeroReclamation(), copy.getNumeroReclamation()) &&
                condition.apply(criteria.getDateReclamation(), copy.getDateReclamation()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getSinistreId(), copy.getSinistreId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
