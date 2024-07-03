package com.teleo.claims.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FournisseurCriteriaTest {

    @Test
    void newFournisseurCriteriaHasAllFiltersNullTest() {
        var fournisseurCriteria = new FournisseurCriteria();
        assertThat(fournisseurCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void fournisseurCriteriaFluentMethodsCreatesFiltersTest() {
        var fournisseurCriteria = new FournisseurCriteria();

        setAllFilters(fournisseurCriteria);

        assertThat(fournisseurCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void fournisseurCriteriaCopyCreatesNullFilterTest() {
        var fournisseurCriteria = new FournisseurCriteria();
        var copy = fournisseurCriteria.copy();

        assertThat(fournisseurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(fournisseurCriteria)
        );
    }

    @Test
    void fournisseurCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var fournisseurCriteria = new FournisseurCriteria();
        setAllFilters(fournisseurCriteria);

        var copy = fournisseurCriteria.copy();

        assertThat(fournisseurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(fournisseurCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var fournisseurCriteria = new FournisseurCriteria();

        assertThat(fournisseurCriteria).hasToString("FournisseurCriteria{}");
    }

    private static void setAllFilters(FournisseurCriteria fournisseurCriteria) {
        fournisseurCriteria.id();
        fournisseurCriteria.nom();
        fournisseurCriteria.adresse();
        fournisseurCriteria.telephone();
        fournisseurCriteria.email();
        fournisseurCriteria.addresse();
        fournisseurCriteria.ville();
        fournisseurCriteria.pays();
        fournisseurCriteria.servicesFournis();
        fournisseurCriteria.dateDebutContrat();
        fournisseurCriteria.dateFinContrat();
        fournisseurCriteria.distinct();
    }

    private static Condition<FournisseurCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getAdresse()) &&
                condition.apply(criteria.getTelephone()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getAddresse()) &&
                condition.apply(criteria.getVille()) &&
                condition.apply(criteria.getPays()) &&
                condition.apply(criteria.getServicesFournis()) &&
                condition.apply(criteria.getDateDebutContrat()) &&
                condition.apply(criteria.getDateFinContrat()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FournisseurCriteria> copyFiltersAre(FournisseurCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getAdresse(), copy.getAdresse()) &&
                condition.apply(criteria.getTelephone(), copy.getTelephone()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getAddresse(), copy.getAddresse()) &&
                condition.apply(criteria.getVille(), copy.getVille()) &&
                condition.apply(criteria.getPays(), copy.getPays()) &&
                condition.apply(criteria.getServicesFournis(), copy.getServicesFournis()) &&
                condition.apply(criteria.getDateDebutContrat(), copy.getDateDebutContrat()) &&
                condition.apply(criteria.getDateFinContrat(), copy.getDateFinContrat()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
