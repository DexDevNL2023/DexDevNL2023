package com.teleo.accounts.service.mapper;

import com.teleo.accounts.domain.User;
import com.teleo.accounts.domain.UserExtra;
import com.teleo.accounts.service.dto.UserDTO;
import com.teleo.accounts.service.dto.UserExtraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserExtra} and its DTO {@link UserExtraDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserExtraMapper extends EntityMapper<UserExtraDTO, UserExtra> {
    @Mapping(target = "internalUser", source = "internalUser", qualifiedByName = "userLogin")
    UserExtraDTO toDto(UserExtra s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
