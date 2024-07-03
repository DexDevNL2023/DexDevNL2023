package com.teleo.insurances.service.mapper;

import com.teleo.insurances.domain.PolicesAssurances;
import com.teleo.insurances.domain.Souscription;
import com.teleo.insurances.service.dto.PolicesAssurancesDTO;
import com.teleo.insurances.service.dto.SouscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Souscription} and its DTO {@link SouscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface SouscriptionMapper extends EntityMapper<SouscriptionDTO, Souscription> {
    @Mapping(target = "policesAssurances", source = "policesAssurances", qualifiedByName = "policesAssurancesId")
    SouscriptionDTO toDto(Souscription s);

    @Named("policesAssurancesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PolicesAssurancesDTO toDtoPolicesAssurancesId(PolicesAssurances policesAssurances);
}
