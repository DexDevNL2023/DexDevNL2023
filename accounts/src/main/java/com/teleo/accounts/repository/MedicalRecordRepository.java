package com.teleo.accounts.repository;

import com.teleo.accounts.domain.MedicalRecord;
import com.teleo.accounts.domain.criteria.MedicalRecordCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the MedicalRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicalRecordRepository extends ReactiveCrudRepository<MedicalRecord, Long>, MedicalRecordRepositoryInternal {
    Flux<MedicalRecord> findAllBy(Pageable pageable);

    @Query("SELECT * FROM medical_record entity WHERE entity.utilisateur_id = :id")
    Flux<MedicalRecord> findByUtilisateur(Long id);

    @Query("SELECT * FROM medical_record entity WHERE entity.utilisateur_id IS NULL")
    Flux<MedicalRecord> findAllWhereUtilisateurIsNull();

    @Override
    <S extends MedicalRecord> Mono<S> save(S entity);

    @Override
    Flux<MedicalRecord> findAll();

    @Override
    Mono<MedicalRecord> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface MedicalRecordRepositoryInternal {
    <S extends MedicalRecord> Mono<S> save(S entity);

    Flux<MedicalRecord> findAllBy(Pageable pageable);

    Flux<MedicalRecord> findAll();

    Mono<MedicalRecord> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<MedicalRecord> findAllBy(Pageable pageable, Criteria criteria);
    Flux<MedicalRecord> findByCriteria(MedicalRecordCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(MedicalRecordCriteria criteria);
}
