package com.teleo.accounts.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UserExtraSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("num_niu", table, columnPrefix + "_num_niu"));
        columns.add(Column.aliased("nom", table, columnPrefix + "_nom"));
        columns.add(Column.aliased("prenom", table, columnPrefix + "_prenom"));
        columns.add(Column.aliased("email", table, columnPrefix + "_email"));
        columns.add(Column.aliased("mot_de_passe", table, columnPrefix + "_mot_de_passe"));
        columns.add(Column.aliased("date_naissance", table, columnPrefix + "_date_naissance"));
        columns.add(Column.aliased("num_cni", table, columnPrefix + "_num_cni"));
        columns.add(Column.aliased("sexe", table, columnPrefix + "_sexe"));
        columns.add(Column.aliased("telephone", table, columnPrefix + "_telephone"));
        columns.add(Column.aliased("addresse", table, columnPrefix + "_addresse"));
        columns.add(Column.aliased("ville", table, columnPrefix + "_ville"));
        columns.add(Column.aliased("pays", table, columnPrefix + "_pays"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("signature", table, columnPrefix + "_signature"));
        columns.add(Column.aliased("signature_content_type", table, columnPrefix + "_signature_content_type"));

        return columns;
    }
}
