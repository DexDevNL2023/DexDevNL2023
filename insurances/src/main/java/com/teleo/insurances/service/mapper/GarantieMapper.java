package com.teleo.insurances.service.mapper;

import com.teleo.insurances.domain.Assurance;
import com.teleo.insurances.domain.Garantie;
import com.teleo.insurances.service.dto.AssuranceDTO;
import com.teleo.insurances.service.dto.GarantieDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Garantie} and its DTO {@link GarantieDTO}.
 */
@Mapper(componentModel = "spring")
public interface GarantieMapper extends EntityMapper<GarantieDTO, Garantie> {
    @Mapping(target = "assurances", source = "assurances", qualifiedByName = "assuranceIdSet")
    GarantieDTO toDto(Garantie s);

    @Mapping(target = "assurances", ignore = true)
    @Mapping(target = "removeAssurance", ignore = true)
    Garantie toEntity(GarantieDTO garantieDTO);

    @Named("assuranceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssuranceDTO toDtoAssuranceId(Assurance assurance);

    @Named("assuranceIdSet")
    default Set<AssuranceDTO> toDtoAssuranceIdSet(Set<Assurance> assurance) {
        return assurance.stream().map(this::toDtoAssuranceId).collect(Collectors.toSet());
    }
}
