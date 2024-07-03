package com.teleo.claims.service.mapper;

import com.teleo.claims.domain.Reclamation;
import com.teleo.claims.domain.Sinistre;
import com.teleo.claims.service.dto.ReclamationDTO;
import com.teleo.claims.service.dto.SinistreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reclamation} and its DTO {@link ReclamationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReclamationMapper extends EntityMapper<ReclamationDTO, Reclamation> {
    @Mapping(target = "sinistre", source = "sinistre", qualifiedByName = "sinistreId")
    ReclamationDTO toDto(Reclamation s);

    @Named("sinistreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SinistreDTO toDtoSinistreId(Sinistre sinistre);
}
