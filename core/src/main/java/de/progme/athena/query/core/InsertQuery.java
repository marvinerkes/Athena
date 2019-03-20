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

import de.progme.athena.query.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Marvin Erkes on 11.08.2015.
 *
 * Represents a SQL INSERT query.
 */
public class InsertQuery implements Query {

    private String table;

    private List<String> columns;

    private List<String> values;

    public InsertQuery(Builder builder) {

        this.table = builder.table;
        this.columns = builder.columns;
        this.values = builder.values;
    }

    @Override
    public String sql() {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(table);

        if(columns.size() > 0) {
            sb.append(" (");
            for (int i = 0; i < columns.size(); i++) {
                if(i < columns.size() - 1) {
                    sb.append(columns.get(i)).append(",");
                }else {
                    sb.append(columns.get(i));
                }
            }
            sb.append(")");
        }

        sb.append(" VALUES ").append("(");

        for (int i = 0; i < values.size(); i++) {
            if(i < values.size() - 1)
                sb.append("?,");
            else
                sb.append("?");
        }

        return sb.append(")").append(";").toString();
    }

    @Override
    public PreparedStatement prepareStatement(Connection connection) {

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql());
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setObject(i + 1, values.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return preparedStatement;
    }

    /**
     * Represents the builder for an insert query.
     */
    public static class Builder {

        private String table;

        private List<String> columns = new ArrayList<>();

        private List<String> values = new ArrayList<>();

        /**
         * Sets the table name in which the query will insert.
         *
         * @param table the table name.
         * @return the builder.
         */
        public Builder into(String table) {

            this.table = table;

            return this;
        }

        /**
         * Adds a parameter list of column names.
         *
         * @param columns the column names.
         * @return the builder.
         */
        public Builder columns(String... columns) {

            this.columns.addAll(Arrays.asList(columns));

            return this;
        }

        /**
         * Adds a column name.
         *
         * @param name the column name.
         * @return the builder.
         */
        public Builder column(String name) {

            this.columns.add(name);

            return this;
        }

        /**
         * Adds a parameter list of values.
         *
         * @param values the values.
         * @return the builder.
         */
        public Builder values(String... values) {

            this.values.addAll(Arrays.asList(values));

            return this;
        }

        /**
         * Adds a value.
         *
         * @param value the value.
         * @return the builder.
         */
        public Builder value(String value) {

            this.values.add(value);

            return this;
        }

        /**
         * Gets the finished InsertQuery.
         *
         * @return the InsertQuery.
         */
        public InsertQuery build() {

            return new InsertQuery(this);
        }
    }
}
