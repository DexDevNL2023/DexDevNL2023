package com.teleo.claims.service.mapper;

import com.teleo.claims.domain.FinanceurSoin;
import com.teleo.claims.domain.PrestationSoin;
import com.teleo.claims.service.dto.FinanceurSoinDTO;
import com.teleo.claims.service.dto.PrestationSoinDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrestationSoin} and its DTO {@link PrestationSoinDTO}.
 */
@Mapper(componentModel = "spring")
public interface PrestationSoinMapper extends EntityMapper<PrestationSoinDTO, PrestationSoin> {
    @Mapping(target = "financeurSoin", source = "financeurSoin", qualifiedByName = "financeurSoinId")
    @Mapping(target = "financeurSoins", source = "financeurSoins", qualifiedByName = "financeurSoinIdSet")
    PrestationSoinDTO toDto(PrestationSoin s);

    @Mapping(target = "removeFinanceurSoin", ignore = true)
    PrestationSoin toEntity(PrestationSoinDTO prestationSoinDTO);

    @Named("financeurSoinId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FinanceurSoinDTO toDtoFinanceurSoinId(FinanceurSoin financeurSoin);

    @Named("financeurSoinIdSet")
    default Set<FinanceurSoinDTO> toDtoFinanceurSoinIdSet(Set<FinanceurSoin> financeurSoin) {
        return financeurSoin.stream().map(this::toDtoFinanceurSoinId).collect(Collectors.toSet());
    }
}
