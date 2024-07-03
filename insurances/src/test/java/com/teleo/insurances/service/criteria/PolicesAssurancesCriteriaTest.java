package com.teleo.insurances.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PolicesAssurancesCriteriaTest {

    @Test
    void newPolicesAssurancesCriteriaHasAllFiltersNullTest() {
        var policesAssurancesCriteria = new PolicesAssurancesCriteria();
        assertThat(policesAssurancesCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void policesAssurancesCriteriaFluentMethodsCreatesFiltersTest() {
        var policesAssurancesCriteria = new PolicesAssurancesCriteria();

        setAllFilters(policesAssurancesCriteria);

        assertThat(policesAssurancesCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void policesAssurancesCriteriaCopyCreatesNullFilterTest() {
        var policesAssurancesCriteria = new PolicesAssurancesCriteria();
        var copy = policesAssurancesCriteria.copy();

        assertThat(policesAssurancesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(policesAssurancesCriteria)
        );
    }

    @Test
    void policesAssurancesCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var policesAssurancesCriteria = new PolicesAssurancesCriteria();
        setAllFilters(policesAssurancesCriteria);

        var copy = policesAssurancesCriteria.copy();

        assertThat(policesAssurancesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(policesAssurancesCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var policesAssurancesCriteria = new PolicesAssurancesCriteria();

        assertThat(policesAssurancesCriteria).hasToString("PolicesAssurancesCriteria{}");
    }

    private static void setAllFilters(PolicesAssurancesCriteria policesAssurancesCriteria) {
        policesAssurancesCriteria.id();
        policesAssurancesCriteria.numeroPolice();
        policesAssurancesCriteria.dateEmission();
        policesAssurancesCriteria.dateExpiration();
        policesAssurancesCriteria.description();
        policesAssurancesCriteria.montantAssure();
        policesAssurancesCriteria.plafondAssure();
        policesAssurancesCriteria.assuranceId();
        policesAssurancesCriteria.distinct();
    }

    private static Condition<PolicesAssurancesCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumeroPolice()) &&
                condition.apply(criteria.getDateEmission()) &&
                condition.apply(criteria.getDateExpiration()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getMontantAssure()) &&
                condition.apply(criteria.getPlafondAssure()) &&
                condition.apply(criteria.getAssuranceId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PolicesAssurancesCriteria> copyFiltersAre(
        PolicesAssurancesCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumeroPolice(), copy.getNumeroPolice()) &&
                condition.apply(criteria.getDateEmission(), copy.getDateEmission()) &&
                condition.apply(criteria.getDateExpiration(), copy.getDateExpiration()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getMontantAssure(), copy.getMontantAssure()) &&
                condition.apply(criteria.getPlafondAssure(), copy.getPlafondAssure()) &&
                condition.apply(criteria.getAssuranceId(), copy.getAssuranceId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
