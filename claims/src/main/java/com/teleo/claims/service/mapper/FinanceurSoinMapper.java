package com.teleo.claims.service.mapper;

import com.teleo.claims.domain.FinanceurSoin;
import com.teleo.claims.domain.PrestationSoin;
import com.teleo.claims.service.dto.FinanceurSoinDTO;
import com.teleo.claims.service.dto.PrestationSoinDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FinanceurSoin} and its DTO {@link FinanceurSoinDTO}.
 */
@Mapper(componentModel = "spring")
public interface FinanceurSoinMapper extends EntityMapper<FinanceurSoinDTO, FinanceurSoin> {
    @Mapping(target = "prestationSoins", source = "prestationSoins", qualifiedByName = "prestationSoinIdSet")
    FinanceurSoinDTO toDto(FinanceurSoin s);

    @Mapping(target = "prestationSoins", ignore = true)
    @Mapping(target = "removePrestationSoin", ignore = true)
    FinanceurSoin toEntity(FinanceurSoinDTO financeurSoinDTO);

    @Named("prestationSoinId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PrestationSoinDTO toDtoPrestationSoinId(PrestationSoin prestationSoin);

    @Named("prestationSoinIdSet")
    default Set<PrestationSoinDTO> toDtoPrestationSoinIdSet(Set<PrestationSoin> prestationSoin) {
        return prestationSoin.stream().map(this::toDtoPrestationSoinId).collect(Collectors.toSet());
    }
}
