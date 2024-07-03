package com.teleo.accounts.service.mapper;

import com.teleo.accounts.domain.MedicalDocument;
import com.teleo.accounts.domain.MedicalRecord;
import com.teleo.accounts.service.dto.MedicalDocumentDTO;
import com.teleo.accounts.service.dto.MedicalRecordDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MedicalDocument} and its DTO {@link MedicalDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicalDocumentMapper extends EntityMapper<MedicalDocumentDTO, MedicalDocument> {
    @Mapping(target = "dossierMedical", source = "dossierMedical", qualifiedByName = "medicalRecordId")
    MedicalDocumentDTO toDto(MedicalDocument s);

    @Named("medicalRecordId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicalRecordDTO toDtoMedicalRecordId(MedicalRecord medicalRecord);
}
