package com.teleo.accounts.service.mapper;

import com.teleo.accounts.domain.Notification;
import com.teleo.accounts.domain.UserExtra;
import com.teleo.accounts.service.dto.NotificationDTO;
import com.teleo.accounts.service.dto.UserExtraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "userExtraId")
    NotificationDTO toDto(Notification s);

    @Named("userExtraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserExtraDTO toDtoUserExtraId(UserExtra userExtra);
}
