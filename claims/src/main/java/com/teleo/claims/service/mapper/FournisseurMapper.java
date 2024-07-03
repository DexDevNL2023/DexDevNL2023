package com.teleo.claims.service.mapper;

import com.teleo.claims.domain.Fournisseur;
import com.teleo.claims.service.dto.FournisseurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fournisseur} and its DTO {@link FournisseurDTO}.
 */
@Mapper(componentModel = "spring")
public interface FournisseurMapper extends EntityMapper<FournisseurDTO, Fournisseur> {}
