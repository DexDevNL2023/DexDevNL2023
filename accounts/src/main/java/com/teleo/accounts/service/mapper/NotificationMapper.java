package com.teleo.accounts.service.mapper;

import com.teleo.accounts.domain.Notification;
import com.teleo.accounts.domain.Utilisateur;
import com.teleo.accounts.service.dto.NotificationDTO;
import com.teleo.accounts.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    NotificationDTO toDto(Notification s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
