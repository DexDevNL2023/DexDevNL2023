package com.teleo.accounts.repository.rowmapper;

import com.teleo.accounts.domain.UserExtra;
import com.teleo.accounts.domain.enumeration.Gender;
import com.teleo.accounts.domain.enumeration.UserExtraType;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserExtra}, with proper type conversions.
 */
@Service
public class UserExtraRowMapper implements BiFunction<Row, String, UserExtra> {

    private final ColumnConverter converter;

    public UserExtraRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserExtra} stored in the database.
     */
    @Override
    public UserExtra apply(Row row, String prefix) {
        UserExtra entity = new UserExtra();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNumNiu(converter.fromRow(row, prefix + "_num_niu", String.class));
        entity.setNom(converter.fromRow(row, prefix + "_nom", String.class));
        entity.setPrenom(converter.fromRow(row, prefix + "_prenom", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setMotDePasse(converter.fromRow(row, prefix + "_mot_de_passe", String.class));
        entity.setDateNaissance(converter.fromRow(row, prefix + "_date_naissance", Instant.class));
        entity.setNumCni(converter.fromRow(row, prefix + "_num_cni", String.class));
        entity.setSexe(converter.fromRow(row, prefix + "_sexe", Gender.class));
        entity.setTelephone(converter.fromRow(row, prefix + "_telephone", String.class));
        entity.setAddresse(converter.fromRow(row, prefix + "_addresse", String.class));
        entity.setVille(converter.fromRow(row, prefix + "_ville", String.class));
        entity.setPays(converter.fromRow(row, prefix + "_pays", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", UserExtraType.class));
        entity.setSignatureContentType(converter.fromRow(row, prefix + "_signature_content_type", String.class));
        entity.setSignature(converter.fromRow(row, prefix + "_signature", byte[].class));
        return entity;
    }
}
