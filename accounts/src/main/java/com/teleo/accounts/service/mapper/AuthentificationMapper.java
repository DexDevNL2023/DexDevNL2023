package com.teleo.accounts.service.mapper;

import com.teleo.accounts.domain.Authentification;
import com.teleo.accounts.service.dto.AuthentificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Authentification} and its DTO {@link AuthentificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthentificationMapper extends EntityMapper<AuthentificationDTO, Authentification> {}
