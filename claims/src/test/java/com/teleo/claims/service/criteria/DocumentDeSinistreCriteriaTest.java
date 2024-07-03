package com.teleo.claims.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentDeSinistreCriteriaTest {

    @Test
    void newDocumentDeSinistreCriteriaHasAllFiltersNullTest() {
        var documentDeSinistreCriteria = new DocumentDeSinistreCriteria();
        assertThat(documentDeSinistreCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void documentDeSinistreCriteriaFluentMethodsCreatesFiltersTest() {
        var documentDeSinistreCriteria = new DocumentDeSinistreCriteria();

        setAllFilters(documentDeSinistreCriteria);

        assertThat(documentDeSinistreCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void documentDeSinistreCriteriaCopyCreatesNullFilterTest() {
        var documentDeSinistreCriteria = new DocumentDeSinistreCriteria();
        var copy = documentDeSinistreCriteria.copy();

        assertThat(documentDeSinistreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(documentDeSinistreCriteria)
        );
    }

    @Test
    void documentDeSinistreCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentDeSinistreCriteria = new DocumentDeSinistreCriteria();
        setAllFilters(documentDeSinistreCriteria);

        var copy = documentDeSinistreCriteria.copy();

        assertThat(documentDeSinistreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(documentDeSinistreCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentDeSinistreCriteria = new DocumentDeSinistreCriteria();

        assertThat(documentDeSinistreCriteria).hasToString("DocumentDeSinistreCriteria{}");
    }

    private static void setAllFilters(DocumentDeSinistreCriteria documentDeSinistreCriteria) {
        documentDeSinistreCriteria.id();
        documentDeSinistreCriteria.nom();
        documentDeSinistreCriteria.type();
        documentDeSinistreCriteria.url();
        documentDeSinistreCriteria.dateTelechargement();
        documentDeSinistreCriteria.sinistreId();
        documentDeSinistreCriteria.distinct();
    }

    private static Condition<DocumentDeSinistreCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getDateTelechargement()) &&
                condition.apply(criteria.getSinistreId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentDeSinistreCriteria> copyFiltersAre(
        DocumentDeSinistreCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getDateTelechargement(), copy.getDateTelechargement()) &&
                condition.apply(criteria.getSinistreId(), copy.getSinistreId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
