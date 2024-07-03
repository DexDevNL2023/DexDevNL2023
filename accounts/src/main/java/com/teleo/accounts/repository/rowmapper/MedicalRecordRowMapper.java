package com.teleo.accounts.repository.rowmapper;

import com.teleo.accounts.domain.MedicalRecord;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link MedicalRecord}, with proper type conversions.
 */
@Service
public class MedicalRecordRowMapper implements BiFunction<Row, String, MedicalRecord> {

    private final ColumnConverter converter;

    public MedicalRecordRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link MedicalRecord} stored in the database.
     */
    @Override
    public MedicalRecord apply(Row row, String prefix) {
        MedicalRecord entity = new MedicalRecord();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNumMedicalRecord(converter.fromRow(row, prefix + "_num_medical_record", String.class));
        entity.setDetails(converter.fromRow(row, prefix + "_details", String.class));
        entity.setDateUpdated(converter.fromRow(row, prefix + "_date_updated", Instant.class));
        entity.setUtilisateurId(converter.fromRow(row, prefix + "_utilisateur_id", Long.class));
        return entity;
    }
}
