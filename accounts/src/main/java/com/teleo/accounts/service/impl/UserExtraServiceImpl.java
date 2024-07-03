package com.teleo.accounts.service.impl;

import com.teleo.accounts.domain.criteria.UserExtraCriteria;
import com.teleo.accounts.repository.UserExtraRepository;
import com.teleo.accounts.repository.UserRepository;
import com.teleo.accounts.service.UserExtraService;
import com.teleo.accounts.service.dto.UserExtraDTO;
import com.teleo.accounts.service.mapper.UserExtraMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.teleo.accounts.domain.UserExtra}.
 */
@Service
@Transactional
public class UserExtraServiceImpl implements UserExtraService {

    private static final Logger log = LoggerFactory.getLogger(UserExtraServiceImpl.class);

    private final UserExtraRepository userExtraRepository;

    private final UserExtraMapper userExtraMapper;

    private final UserRepository userRepository;

    public UserExtraServiceImpl(UserExtraRepository userExtraRepository, UserExtraMapper userExtraMapper, UserRepository userRepository) {
        this.userExtraRepository = userExtraRepository;
        this.userExtraMapper = userExtraMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserExtraDTO> save(UserExtraDTO userExtraDTO) {
        log.debug("Request to save UserExtra : {}", userExtraDTO);
        return userExtraRepository.save(userExtraMapper.toEntity(userExtraDTO)).map(userExtraMapper::toDto);
    }

    @Override
    public Mono<UserExtraDTO> update(UserExtraDTO userExtraDTO) {
        log.debug("Request to update UserExtra : {}", userExtraDTO);
        return userExtraRepository.save(userExtraMapper.toEntity(userExtraDTO)).map(userExtraMapper::toDto);
    }

    @Override
    public Mono<UserExtraDTO> partialUpdate(UserExtraDTO userExtraDTO) {
        log.debug("Request to partially update UserExtra : {}", userExtraDTO);

        return userExtraRepository
            .findById(userExtraDTO.getId())
            .map(existingUserExtra -> {
                userExtraMapper.partialUpdate(existingUserExtra, userExtraDTO);

                return existingUserExtra;
            })
            .flatMap(userExtraRepository::save)
            .map(userExtraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserExtraDTO> findByCriteria(UserExtraCriteria criteria, Pageable pageable) {
        log.debug("Request to get all UserExtras by Criteria");
        return userExtraRepository.findByCriteria(criteria, pageable).map(userExtraMapper::toDto);
    }

    /**
     * Find the count of userExtras by criteria.
     * @param criteria filtering criteria
     * @return the count of userExtras
     */
    public Mono<Long> countByCriteria(UserExtraCriteria criteria) {
        log.debug("Request to get the count of all UserExtras by Criteria");
        return userExtraRepository.countByCriteria(criteria);
    }

    public Flux<UserExtraDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userExtraRepository.findAllWithEagerRelationships(pageable).map(userExtraMapper::toDto);
    }

    public Mono<Long> countAll() {
        return userExtraRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserExtraDTO> findOne(Long id) {
        log.debug("Request to get UserExtra : {}", id);
        return userExtraRepository.findOneWithEagerRelationships(id).map(userExtraMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserExtra : {}", id);
        return userExtraRepository.deleteById(id);
    }
}
