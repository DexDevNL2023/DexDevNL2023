package com.teleo.claims.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FinanceurSoinCriteriaTest {

    @Test
    void newFinanceurSoinCriteriaHasAllFiltersNullTest() {
        var financeurSoinCriteria = new FinanceurSoinCriteria();
        assertThat(financeurSoinCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void financeurSoinCriteriaFluentMethodsCreatesFiltersTest() {
        var financeurSoinCriteria = new FinanceurSoinCriteria();

        setAllFilters(financeurSoinCriteria);

        assertThat(financeurSoinCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void financeurSoinCriteriaCopyCreatesNullFilterTest() {
        var financeurSoinCriteria = new FinanceurSoinCriteria();
        var copy = financeurSoinCriteria.copy();

        assertThat(financeurSoinCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(financeurSoinCriteria)
        );
    }

    @Test
    void financeurSoinCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var financeurSoinCriteria = new FinanceurSoinCriteria();
        setAllFilters(financeurSoinCriteria);

        var copy = financeurSoinCriteria.copy();

        assertThat(financeurSoinCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(financeurSoinCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var financeurSoinCriteria = new FinanceurSoinCriteria();

        assertThat(financeurSoinCriteria).hasToString("FinanceurSoinCriteria{}");
    }

    private static void setAllFilters(FinanceurSoinCriteria financeurSoinCriteria) {
        financeurSoinCriteria.id();
        financeurSoinCriteria.nom();
        financeurSoinCriteria.type();
        financeurSoinCriteria.adresse();
        financeurSoinCriteria.telephone();
        financeurSoinCriteria.email();
        financeurSoinCriteria.servicesFournis();
        financeurSoinCriteria.prestationSoinId();
        financeurSoinCriteria.distinct();
    }

    private static Condition<FinanceurSoinCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getAdresse()) &&
                condition.apply(criteria.getTelephone()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getServicesFournis()) &&
                condition.apply(criteria.getPrestationSoinId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FinanceurSoinCriteria> copyFiltersAre(
        FinanceurSoinCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getAdresse(), copy.getAdresse()) &&
                condition.apply(criteria.getTelephone(), copy.getTelephone()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getServicesFournis(), copy.getServicesFournis()) &&
                condition.apply(criteria.getPrestationSoinId(), copy.getPrestationSoinId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
