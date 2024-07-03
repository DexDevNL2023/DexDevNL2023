package com.teleo.claims.service.mapper;

import com.teleo.claims.domain.DocumentDeSinistre;
import com.teleo.claims.domain.Sinistre;
import com.teleo.claims.service.dto.DocumentDeSinistreDTO;
import com.teleo.claims.service.dto.SinistreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentDeSinistre} and its DTO {@link DocumentDeSinistreDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentDeSinistreMapper extends EntityMapper<DocumentDeSinistreDTO, DocumentDeSinistre> {
    @Mapping(target = "sinistre", source = "sinistre", qualifiedByName = "sinistreId")
    DocumentDeSinistreDTO toDto(DocumentDeSinistre s);

    @Named("sinistreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SinistreDTO toDtoSinistreId(Sinistre sinistre);
}
