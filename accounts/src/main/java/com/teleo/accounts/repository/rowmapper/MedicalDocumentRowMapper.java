package com.teleo.accounts.repository.rowmapper;

import com.teleo.accounts.domain.MedicalDocument;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link MedicalDocument}, with proper type conversions.
 */
@Service
public class MedicalDocumentRowMapper implements BiFunction<Row, String, MedicalDocument> {

    private final ColumnConverter converter;

    public MedicalDocumentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link MedicalDocument} stored in the database.
     */
    @Override
    public MedicalDocument apply(Row row, String prefix) {
        MedicalDocument entity = new MedicalDocument();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNom(converter.fromRow(row, prefix + "_nom", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setUrl(converter.fromRow(row, prefix + "_url", String.class));
        entity.setDossierMedicalId(converter.fromRow(row, prefix + "_dossier_medical_id", Long.class));
        return entity;
    }
}
