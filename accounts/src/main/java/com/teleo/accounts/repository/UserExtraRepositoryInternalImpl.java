package com.teleo.accounts.repository;

import com.teleo.accounts.domain.UserExtra;
import com.teleo.accounts.domain.criteria.UserExtraCriteria;
import com.teleo.accounts.repository.rowmapper.ColumnConverter;
import com.teleo.accounts.repository.rowmapper.UserExtraRowMapper;
import com.teleo.accounts.repository.rowmapper.UserRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the UserExtra entity.
 */
@SuppressWarnings("unused")
class UserExtraRepositoryInternalImpl extends SimpleR2dbcRepository<UserExtra, Long> implements UserExtraRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final UserExtraRowMapper userextraMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("user_extra", EntityManager.ENTITY_ALIAS);
    private static final Table internalUserTable = Table.aliased("jhi_user", "internalUser");

    public UserExtraRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        UserExtraRowMapper userextraMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UserExtra.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.userextraMapper = userextraMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<UserExtra> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserExtra> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserExtraSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(internalUserTable, "internalUser"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(internalUserTable)
            .on(Column.create("id", entityTable))
            .equals(Column.create("id", internalUserTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UserExtra.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserExtra> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserExtra> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<UserExtra> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<UserExtra> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<UserExtra> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private UserExtra process(Row row, RowMetadata metadata) {
        UserExtra entity = userextraMapper.apply(row, "e");
        entity.setInternalUser(userMapper.apply(row, "internalUser"));
        return entity;
    }

    @Override
    public <S extends UserExtra> Mono<S> save(S entity) {
        if (entity.getId() == null && entity.getInternalUser() != null) {
            entity.setId(entity.getInternalUser().getId());
            return entityManager.insert(entity);
        }
        return super.save(entity);
    }

    @Override
    public Flux<UserExtra> findByCriteria(UserExtraCriteria userExtraCriteria, Pageable page) {
        return createQuery(page, buildConditions(userExtraCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(UserExtraCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(UserExtraCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getNumNiu() != null) {
                builder.buildFilterConditionForField(criteria.getNumNiu(), entityTable.column("num_niu"));
            }
            if (criteria.getNom() != null) {
                builder.buildFilterConditionForField(criteria.getNom(), entityTable.column("nom"));
            }
            if (criteria.getPrenom() != null) {
                builder.buildFilterConditionForField(criteria.getPrenom(), entityTable.column("prenom"));
            }
            if (criteria.getEmail() != null) {
                builder.buildFilterConditionForField(criteria.getEmail(), entityTable.column("email"));
            }
            if (criteria.getMotDePasse() != null) {
                builder.buildFilterConditionForField(criteria.getMotDePasse(), entityTable.column("mot_de_passe"));
            }
            if (criteria.getDateNaissance() != null) {
                builder.buildFilterConditionForField(criteria.getDateNaissance(), entityTable.column("date_naissance"));
            }
            if (criteria.getNumCni() != null) {
                builder.buildFilterConditionForField(criteria.getNumCni(), entityTable.column("num_cni"));
            }
            if (criteria.getSexe() != null) {
                builder.buildFilterConditionForField(criteria.getSexe(), entityTable.column("sexe"));
            }
            if (criteria.getTelephone() != null) {
                builder.buildFilterConditionForField(criteria.getTelephone(), entityTable.column("telephone"));
            }
            if (criteria.getAddresse() != null) {
                builder.buildFilterConditionForField(criteria.getAddresse(), entityTable.column("addresse"));
            }
            if (criteria.getVille() != null) {
                builder.buildFilterConditionForField(criteria.getVille(), entityTable.column("ville"));
            }
            if (criteria.getPays() != null) {
                builder.buildFilterConditionForField(criteria.getPays(), entityTable.column("pays"));
            }
            if (criteria.getType() != null) {
                builder.buildFilterConditionForField(criteria.getType(), entityTable.column("type"));
            }
            if (criteria.getInternalUserId() != null) {
                builder.buildFilterConditionForField(criteria.getInternalUserId(), internalUserTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
