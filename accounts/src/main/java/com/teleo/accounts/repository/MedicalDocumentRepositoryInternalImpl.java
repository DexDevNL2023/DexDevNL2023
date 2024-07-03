package com.teleo.accounts.repository;

import com.teleo.accounts.domain.MedicalDocument;
import com.teleo.accounts.domain.criteria.MedicalDocumentCriteria;
import com.teleo.accounts.repository.rowmapper.ColumnConverter;
import com.teleo.accounts.repository.rowmapper.MedicalDocumentRowMapper;
import com.teleo.accounts.repository.rowmapper.MedicalRecordRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the MedicalDocument entity.
 */
@SuppressWarnings("unused")
class MedicalDocumentRepositoryInternalImpl
    extends SimpleR2dbcRepository<MedicalDocument, Long>
    implements MedicalDocumentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final MedicalRecordRowMapper medicalrecordMapper;
    private final MedicalDocumentRowMapper medicaldocumentMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("medical_document", EntityManager.ENTITY_ALIAS);
    private static final Table dossierMedicalTable = Table.aliased("medical_record", "dossierMedical");

    public MedicalDocumentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        MedicalRecordRowMapper medicalrecordMapper,
        MedicalDocumentRowMapper medicaldocumentMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(MedicalDocument.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.medicalrecordMapper = medicalrecordMapper;
        this.medicaldocumentMapper = medicaldocumentMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<MedicalDocument> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<MedicalDocument> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MedicalDocumentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(MedicalRecordSqlHelper.getColumns(dossierMedicalTable, "dossierMedical"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(dossierMedicalTable)
            .on(Column.create("dossier_medical_id", entityTable))
            .equals(Column.create("id", dossierMedicalTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, MedicalDocument.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<MedicalDocument> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<MedicalDocument> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private MedicalDocument process(Row row, RowMetadata metadata) {
        MedicalDocument entity = medicaldocumentMapper.apply(row, "e");
        entity.setDossierMedical(medicalrecordMapper.apply(row, "dossierMedical"));
        return entity;
    }

    @Override
    public <S extends MedicalDocument> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<MedicalDocument> findByCriteria(MedicalDocumentCriteria medicalDocumentCriteria, Pageable page) {
        return createQuery(page, buildConditions(medicalDocumentCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(MedicalDocumentCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(MedicalDocumentCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getNom() != null) {
                builder.buildFilterConditionForField(criteria.getNom(), entityTable.column("nom"));
            }
            if (criteria.getType() != null) {
                builder.buildFilterConditionForField(criteria.getType(), entityTable.column("type"));
            }
            if (criteria.getUrl() != null) {
                builder.buildFilterConditionForField(criteria.getUrl(), entityTable.column("url"));
            }
            if (criteria.getDossierMedicalId() != null) {
                builder.buildFilterConditionForField(criteria.getDossierMedicalId(), dossierMedicalTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
