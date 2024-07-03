package com.teleo.insurances.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RapportCriteriaTest {

    @Test
    void newRapportCriteriaHasAllFiltersNullTest() {
        var rapportCriteria = new RapportCriteria();
        assertThat(rapportCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void rapportCriteriaFluentMethodsCreatesFiltersTest() {
        var rapportCriteria = new RapportCriteria();

        setAllFilters(rapportCriteria);

        assertThat(rapportCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void rapportCriteriaCopyCreatesNullFilterTest() {
        var rapportCriteria = new RapportCriteria();
        var copy = rapportCriteria.copy();

        assertThat(rapportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(rapportCriteria)
        );
    }

    @Test
    void rapportCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var rapportCriteria = new RapportCriteria();
        setAllFilters(rapportCriteria);

        var copy = rapportCriteria.copy();

        assertThat(rapportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(rapportCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var rapportCriteria = new RapportCriteria();

        assertThat(rapportCriteria).hasToString("RapportCriteria{}");
    }

    private static void setAllFilters(RapportCriteria rapportCriteria) {
        rapportCriteria.id();
        rapportCriteria.titre();
        rapportCriteria.description();
        rapportCriteria.type();
        rapportCriteria.dateGeneration();
        rapportCriteria.url();
        rapportCriteria.assuranceId();
        rapportCriteria.distinct();
    }

    private static Condition<RapportCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitre()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getDateGeneration()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getAssuranceId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RapportCriteria> copyFiltersAre(RapportCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitre(), copy.getTitre()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getDateGeneration(), copy.getDateGeneration()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getAssuranceId(), copy.getAssuranceId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
