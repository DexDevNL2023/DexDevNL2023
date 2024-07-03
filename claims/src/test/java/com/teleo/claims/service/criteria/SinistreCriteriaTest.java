package com.teleo.claims.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SinistreCriteriaTest {

    @Test
    void newSinistreCriteriaHasAllFiltersNullTest() {
        var sinistreCriteria = new SinistreCriteria();
        assertThat(sinistreCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void sinistreCriteriaFluentMethodsCreatesFiltersTest() {
        var sinistreCriteria = new SinistreCriteria();

        setAllFilters(sinistreCriteria);

        assertThat(sinistreCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void sinistreCriteriaCopyCreatesNullFilterTest() {
        var sinistreCriteria = new SinistreCriteria();
        var copy = sinistreCriteria.copy();

        assertThat(sinistreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(sinistreCriteria)
        );
    }

    @Test
    void sinistreCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var sinistreCriteria = new SinistreCriteria();
        setAllFilters(sinistreCriteria);

        var copy = sinistreCriteria.copy();

        assertThat(sinistreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(sinistreCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var sinistreCriteria = new SinistreCriteria();

        assertThat(sinistreCriteria).hasToString("SinistreCriteria{}");
    }

    private static void setAllFilters(SinistreCriteria sinistreCriteria) {
        sinistreCriteria.id();
        sinistreCriteria.numeroSinistre();
        sinistreCriteria.dateDeclaration();
        sinistreCriteria.dateTraitement();
        sinistreCriteria.status();
        sinistreCriteria.raison();
        sinistreCriteria.montantReclame();
        sinistreCriteria.montantApprouve();
        sinistreCriteria.souscriptionId();
        sinistreCriteria.fournisseurId();
        sinistreCriteria.prestationsSoinId();
        sinistreCriteria.distinct();
    }

    private static Condition<SinistreCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumeroSinistre()) &&
                condition.apply(criteria.getDateDeclaration()) &&
                condition.apply(criteria.getDateTraitement()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getRaison()) &&
                condition.apply(criteria.getMontantReclame()) &&
                condition.apply(criteria.getMontantApprouve()) &&
                condition.apply(criteria.getSouscriptionId()) &&
                condition.apply(criteria.getFournisseurId()) &&
                condition.apply(criteria.getPrestationsSoinId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SinistreCriteria> copyFiltersAre(SinistreCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumeroSinistre(), copy.getNumeroSinistre()) &&
                condition.apply(criteria.getDateDeclaration(), copy.getDateDeclaration()) &&
                condition.apply(criteria.getDateTraitement(), copy.getDateTraitement()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getRaison(), copy.getRaison()) &&
                condition.apply(criteria.getMontantReclame(), copy.getMontantReclame()) &&
                condition.apply(criteria.getMontantApprouve(), copy.getMontantApprouve()) &&
                condition.apply(criteria.getSouscriptionId(), copy.getSouscriptionId()) &&
                condition.apply(criteria.getFournisseurId(), copy.getFournisseurId()) &&
                condition.apply(criteria.getPrestationsSoinId(), copy.getPrestationsSoinId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
