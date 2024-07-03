package com.teleo.accounts.repository;

import com.teleo.accounts.domain.MedicalRecord;
import com.teleo.accounts.domain.criteria.MedicalRecordCriteria;
import com.teleo.accounts.repository.rowmapper.ColumnConverter;
import com.teleo.accounts.repository.rowmapper.MedicalRecordRowMapper;
import com.teleo.accounts.repository.rowmapper.UserExtraRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the MedicalRecord entity.
 */
@SuppressWarnings("unused")
class MedicalRecordRepositoryInternalImpl extends SimpleR2dbcRepository<MedicalRecord, Long> implements MedicalRecordRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserExtraRowMapper userextraMapper;
    private final MedicalRecordRowMapper medicalrecordMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("medical_record", EntityManager.ENTITY_ALIAS);
    private static final Table utilisateurTable = Table.aliased("user_extra", "utilisateur");

    public MedicalRecordRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserExtraRowMapper userextraMapper,
        MedicalRecordRowMapper medicalrecordMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(MedicalRecord.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userextraMapper = userextraMapper;
        this.medicalrecordMapper = medicalrecordMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<MedicalRecord> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<MedicalRecord> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MedicalRecordSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserExtraSqlHelper.getColumns(utilisateurTable, "utilisateur"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(utilisateurTable)
            .on(Column.create("utilisateur_id", entityTable))
            .equals(Column.create("id", utilisateurTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, MedicalRecord.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<MedicalRecord> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<MedicalRecord> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private MedicalRecord process(Row row, RowMetadata metadata) {
        MedicalRecord entity = medicalrecordMapper.apply(row, "e");
        entity.setUtilisateur(userextraMapper.apply(row, "utilisateur"));
        return entity;
    }

    @Override
    public <S extends MedicalRecord> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<MedicalRecord> findByCriteria(MedicalRecordCriteria medicalRecordCriteria, Pageable page) {
        return createQuery(page, buildConditions(medicalRecordCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(MedicalRecordCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(MedicalRecordCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getNumMedicalRecord() != null) {
                builder.buildFilterConditionForField(criteria.getNumMedicalRecord(), entityTable.column("num_medical_record"));
            }
            if (criteria.getDetails() != null) {
                builder.buildFilterConditionForField(criteria.getDetails(), entityTable.column("details"));
            }
            if (criteria.getDateUpdated() != null) {
                builder.buildFilterConditionForField(criteria.getDateUpdated(), entityTable.column("date_updated"));
            }
            if (criteria.getUtilisateurId() != null) {
                builder.buildFilterConditionForField(criteria.getUtilisateurId(), utilisateurTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
