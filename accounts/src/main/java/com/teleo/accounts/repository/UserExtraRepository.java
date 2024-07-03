package com.teleo.accounts.repository;

import com.teleo.accounts.domain.UserExtra;
import com.teleo.accounts.domain.criteria.UserExtraCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserExtra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserExtraRepository extends ReactiveCrudRepository<UserExtra, Long>, UserExtraRepositoryInternal {
    Flux<UserExtra> findAllBy(Pageable pageable);

    @Override
    Mono<UserExtra> findOneWithEagerRelationships(Long id);

    @Override
    Flux<UserExtra> findAllWithEagerRelationships();

    @Override
    Flux<UserExtra> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM user_extra entity WHERE entity.id = :id")
    Flux<UserExtra> findByInternalUser(Long id);

    @Query("SELECT * FROM user_extra entity WHERE entity.id IS NULL")
    Flux<UserExtra> findAllWhereInternalUserIsNull();

    @Override
    <S extends UserExtra> Mono<S> save(S entity);

    @Override
    Flux<UserExtra> findAll();

    @Override
    Mono<UserExtra> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserExtraRepositoryInternal {
    <S extends UserExtra> Mono<S> save(S entity);

    Flux<UserExtra> findAllBy(Pageable pageable);

    Flux<UserExtra> findAll();

    Mono<UserExtra> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserExtra> findAllBy(Pageable pageable, Criteria criteria);
    Flux<UserExtra> findByCriteria(UserExtraCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(UserExtraCriteria criteria);

    Mono<UserExtra> findOneWithEagerRelationships(Long id);

    Flux<UserExtra> findAllWithEagerRelationships();

    Flux<UserExtra> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
