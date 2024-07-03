package com.teleo.insurances.service.mapper;

import com.teleo.insurances.domain.Assurance;
import com.teleo.insurances.domain.Rapport;
import com.teleo.insurances.service.dto.AssuranceDTO;
import com.teleo.insurances.service.dto.RapportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Rapport} and its DTO {@link RapportDTO}.
 */
@Mapper(componentModel = "spring")
public interface RapportMapper extends EntityMapper<RapportDTO, Rapport> {
    @Mapping(target = "assurance", source = "assurance", qualifiedByName = "assuranceId")
    RapportDTO toDto(Rapport s);

    @Named("assuranceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssuranceDTO toDtoAssuranceId(Assurance assurance);
}
