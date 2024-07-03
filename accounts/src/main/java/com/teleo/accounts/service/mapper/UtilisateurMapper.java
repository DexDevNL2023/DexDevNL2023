package com.teleo.accounts.service.mapper;

import com.teleo.accounts.domain.Authentification;
import com.teleo.accounts.domain.Utilisateur;
import com.teleo.accounts.service.dto.AuthentificationDTO;
import com.teleo.accounts.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Utilisateur} and its DTO {@link UtilisateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilisateurMapper extends EntityMapper<UtilisateurDTO, Utilisateur> {
    @Mapping(target = "authentification", source = "authentification", qualifiedByName = "authentificationId")
    UtilisateurDTO toDto(Utilisateur s);

    @Named("authentificationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AuthentificationDTO toDtoAuthentificationId(Authentification authentification);
}
