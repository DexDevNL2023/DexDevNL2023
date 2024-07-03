package com.teleo.insurances.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GarantieCriteriaTest {

    @Test
    void newGarantieCriteriaHasAllFiltersNullTest() {
        var garantieCriteria = new GarantieCriteria();
        assertThat(garantieCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void garantieCriteriaFluentMethodsCreatesFiltersTest() {
        var garantieCriteria = new GarantieCriteria();

        setAllFilters(garantieCriteria);

        assertThat(garantieCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void garantieCriteriaCopyCreatesNullFilterTest() {
        var garantieCriteria = new GarantieCriteria();
        var copy = garantieCriteria.copy();

        assertThat(garantieCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(garantieCriteria)
        );
    }

    @Test
    void garantieCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var garantieCriteria = new GarantieCriteria();
        setAllFilters(garantieCriteria);

        var copy = garantieCriteria.copy();

        assertThat(garantieCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(garantieCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var garantieCriteria = new GarantieCriteria();

        assertThat(garantieCriteria).hasToString("GarantieCriteria{}");
    }

    private static void setAllFilters(GarantieCriteria garantieCriteria) {
        garantieCriteria.id();
        garantieCriteria.description();
        garantieCriteria.dateDebut();
        garantieCriteria.dateFin();
        garantieCriteria.status();
        garantieCriteria.assuranceId();
        garantieCriteria.distinct();
    }

    private static Condition<GarantieCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDateDebut()) &&
                condition.apply(criteria.getDateFin()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getAssuranceId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<GarantieCriteria> copyFiltersAre(GarantieCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDateDebut(), copy.getDateDebut()) &&
                condition.apply(criteria.getDateFin(), copy.getDateFin()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getAssuranceId(), copy.getAssuranceId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
