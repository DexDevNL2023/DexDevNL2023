package com.teleo.insurances.service.mapper;

import com.teleo.insurances.domain.Assurance;
import com.teleo.insurances.domain.Garantie;
import com.teleo.insurances.service.dto.AssuranceDTO;
import com.teleo.insurances.service.dto.GarantieDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Assurance} and its DTO {@link AssuranceDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssuranceMapper extends EntityMapper<AssuranceDTO, Assurance> {
    @Mapping(target = "garanties", source = "garanties", qualifiedByName = "garantieIdSet")
    AssuranceDTO toDto(Assurance s);

    @Mapping(target = "removeGarantie", ignore = true)
    Assurance toEntity(AssuranceDTO assuranceDTO);

    @Named("garantieId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GarantieDTO toDtoGarantieId(Garantie garantie);

    @Named("garantieIdSet")
    default Set<GarantieDTO> toDtoGarantieIdSet(Set<Garantie> garantie) {
        return garantie.stream().map(this::toDtoGarantieId).collect(Collectors.toSet());
    }
}
