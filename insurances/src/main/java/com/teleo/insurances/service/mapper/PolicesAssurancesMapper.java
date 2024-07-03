package com.teleo.insurances.service.mapper;

import com.teleo.insurances.domain.Assurance;
import com.teleo.insurances.domain.PolicesAssurances;
import com.teleo.insurances.service.dto.AssuranceDTO;
import com.teleo.insurances.service.dto.PolicesAssurancesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PolicesAssurances} and its DTO {@link PolicesAssurancesDTO}.
 */
@Mapper(componentModel = "spring")
public interface PolicesAssurancesMapper extends EntityMapper<PolicesAssurancesDTO, PolicesAssurances> {
    @Mapping(target = "assurance", source = "assurance", qualifiedByName = "assuranceId")
    PolicesAssurancesDTO toDto(PolicesAssurances s);

    @Named("assuranceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssuranceDTO toDtoAssuranceId(Assurance assurance);
}
