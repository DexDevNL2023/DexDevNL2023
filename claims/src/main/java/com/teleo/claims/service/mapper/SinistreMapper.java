package com.teleo.claims.service.mapper;

import com.teleo.claims.domain.Fournisseur;
import com.teleo.claims.domain.PrestationSoin;
import com.teleo.claims.domain.Sinistre;
import com.teleo.claims.service.dto.FournisseurDTO;
import com.teleo.claims.service.dto.PrestationSoinDTO;
import com.teleo.claims.service.dto.SinistreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sinistre} and its DTO {@link SinistreDTO}.
 */
@Mapper(componentModel = "spring")
public interface SinistreMapper extends EntityMapper<SinistreDTO, Sinistre> {
    @Mapping(target = "fournisseur", source = "fournisseur", qualifiedByName = "fournisseurId")
    @Mapping(target = "prestationsSoin", source = "prestationsSoin", qualifiedByName = "prestationSoinId")
    SinistreDTO toDto(Sinistre s);

    @Named("fournisseurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FournisseurDTO toDtoFournisseurId(Fournisseur fournisseur);

    @Named("prestationSoinId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PrestationSoinDTO toDtoPrestationSoinId(PrestationSoin prestationSoin);
}
