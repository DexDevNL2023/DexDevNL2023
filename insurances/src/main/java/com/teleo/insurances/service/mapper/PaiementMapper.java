package com.teleo.insurances.service.mapper;

import com.teleo.insurances.domain.Paiement;
import com.teleo.insurances.domain.Souscription;
import com.teleo.insurances.service.dto.PaiementDTO;
import com.teleo.insurances.service.dto.SouscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paiement} and its DTO {@link PaiementDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaiementMapper extends EntityMapper<PaiementDTO, Paiement> {
    @Mapping(target = "souscription", source = "souscription", qualifiedByName = "souscriptionId")
    PaiementDTO toDto(Paiement s);

    @Named("souscriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SouscriptionDTO toDtoSouscriptionId(Souscription souscription);
}
