package com.teleo.accounts.service.mapper;

import com.teleo.accounts.domain.MedicalRecord;
import com.teleo.accounts.domain.Utilisateur;
import com.teleo.accounts.service.dto.MedicalRecordDTO;
import com.teleo.accounts.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MedicalRecord} and its DTO {@link MedicalRecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicalRecordMapper extends EntityMapper<MedicalRecordDTO, MedicalRecord> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    MedicalRecordDTO toDto(MedicalRecord s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
