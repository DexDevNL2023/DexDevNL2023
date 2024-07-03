package com.teleo.claims.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PrestationSoinCriteriaTest {

    @Test
    void newPrestationSoinCriteriaHasAllFiltersNullTest() {
        var prestationSoinCriteria = new PrestationSoinCriteria();
        assertThat(prestationSoinCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void prestationSoinCriteriaFluentMethodsCreatesFiltersTest() {
        var prestationSoinCriteria = new PrestationSoinCriteria();

        setAllFilters(prestationSoinCriteria);

        assertThat(prestationSoinCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void prestationSoinCriteriaCopyCreatesNullFilterTest() {
        var prestationSoinCriteria = new PrestationSoinCriteria();
        var copy = prestationSoinCriteria.copy();

        assertThat(prestationSoinCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(prestationSoinCriteria)
        );
    }

    @Test
    void prestationSoinCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var prestationSoinCriteria = new PrestationSoinCriteria();
        setAllFilters(prestationSoinCriteria);

        var copy = prestationSoinCriteria.copy();

        assertThat(prestationSoinCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(prestationSoinCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var prestationSoinCriteria = new PrestationSoinCriteria();

        assertThat(prestationSoinCriteria).hasToString("PrestationSoinCriteria{}");
    }

    private static void setAllFilters(PrestationSoinCriteria prestationSoinCriteria) {
        prestationSoinCriteria.id();
        prestationSoinCriteria.nom();
        prestationSoinCriteria.description();
        prestationSoinCriteria.type();
        prestationSoinCriteria.montant();
        prestationSoinCriteria.financeurSoinId();
        prestationSoinCriteria.financeurSoinId();
        prestationSoinCriteria.distinct();
    }

    private static Condition<PrestationSoinCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getMontant()) &&
                condition.apply(criteria.getFinanceurSoinId()) &&
                condition.apply(criteria.getFinanceurSoinId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PrestationSoinCriteria> copyFiltersAre(
        PrestationSoinCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getMontant(), copy.getMontant()) &&
                condition.apply(criteria.getFinanceurSoinId(), copy.getFinanceurSoinId()) &&
                condition.apply(criteria.getFinanceurSoinId(), copy.getFinanceurSoinId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
