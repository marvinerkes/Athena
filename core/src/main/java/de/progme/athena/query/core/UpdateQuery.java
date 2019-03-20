/*
 * Copyright (c) 2017 "Marvin Erkes"
 *
 * This file is part of Athena.
 *
 * Athena is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.progme.athena.query.core;

import de.progme.athena.db.serialization.Condition;
import de.progme.athena.query.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Marvin Erkes on 11.08.2015.
 *
 * Represents a SQL UPDATE query.
 */
public class UpdateQuery implements Query {

    private String table;

    private LinkedHashMap<String, String> values = new LinkedHashMap<>();

    private LinkedHashMap<String, String> wheres = new LinkedHashMap<>();

    private List<String> whereOperators = new ArrayList<>();

    public UpdateQuery(Builder builder) {

        this.table = builder.table;
        this.values = builder.values;
        this.wheres = builder.wheres;
        this.whereOperators = builder.whereOperators;
    }

    @Override
    public String sql() {

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(table);

        if(values.size() > 0) {
            sb.append(" SET ");

            int i = 0;
            for (String valueKey : values.keySet()) {
                sb.append(valueKey).append("=").append("?").append((i < values.size() - 1) ? "," : "");
                i++;
            }
        }

        if(wheres.size() > 0) {
            sb.append(" WHERE ");

            int i = 0;
            for (String whereKey : wheres.keySet()) {
                sb.append(whereKey).append(whereOperators.get(i)).append("?").append((i < wheres.size() - 1) ? " AND " : "");
                i++;
            }
        }

        return sb.append(";").toString();
    }

    @Override
    public PreparedStatement prepareStatement(Connection connection) {

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql());
            List<String> valueList = new ArrayList<>(values.values());
            int offset = 0;
            for (int j = 0; j < valueList.size(); j++) {
                preparedStatement.setObject(j + 1, valueList.get(j));

                offset++;
            }

            List<String> wheresList = new ArrayList<>(wheres.values());
            for (int k = 0; k < wheresList.size(); k++) {
                preparedStatement.setObject(k + offset + 1, wheresList.get(k));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return preparedStatement;
    }

    /**
     * Represents the builder for an update query.
     */
    public static class Builder {

        private String table;

        private LinkedHashMap<String, String> values = new LinkedHashMap<>();

        private LinkedHashMap<String, String> wheres = new LinkedHashMap<>();

        private List<String> whereOperators = new ArrayList<>();

        /**
         * Sets the table that will be effected.
         *
         * @param table the table name.
         * @return the builder.
         */
        public Builder update(String table) {

            this.table = table;

            return this;
        }

        /**
         * Adds an column name and the new value.
         *
         * @param columnName the column name.
         * @param newValue the new value.
         * @return the builder.
         */
        public Builder set(String columnName, String newValue) {

            this.values.put(columnName, newValue);

            return this;
        }

        /**
         * Adds a where statement with the given condition.
         *
         * @param condition the condition.
         * @return the builder.
         */
        public Builder where(Condition condition) {

            this.wheres.put(condition.column(), condition.value());
            this.whereOperators.add(condition.operator().sql());

            return this;
        }

        /**
         * Gets the finished UpdateQuery.
         *
         * @return the UpdateQuery.
         */
        public UpdateQuery build() {

            return new UpdateQuery(this);
        }
    }
}
