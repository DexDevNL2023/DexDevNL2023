package com.teleo.accounts.repository;

import com.teleo.accounts.domain.Notification;
import com.teleo.accounts.domain.criteria.NotificationCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends ReactiveCrudRepository<Notification, Long>, NotificationRepositoryInternal {
    Flux<Notification> findAllBy(Pageable pageable);

    @Query("SELECT * FROM notification entity WHERE entity.utilisateur_id = :id")
    Flux<Notification> findByUtilisateur(Long id);

    @Query("SELECT * FROM notification entity WHERE entity.utilisateur_id IS NULL")
    Flux<Notification> findAllWhereUtilisateurIsNull();

    @Override
    <S extends Notification> Mono<S> save(S entity);

    @Override
    Flux<Notification> findAll();

    @Override
    Mono<Notification> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface NotificationRepositoryInternal {
    <S extends Notification> Mono<S> save(S entity);

    Flux<Notification> findAllBy(Pageable pageable);

    Flux<Notification> findAll();

    Mono<Notification> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Notification> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Notification> findByCriteria(NotificationCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(NotificationCriteria criteria);
}
