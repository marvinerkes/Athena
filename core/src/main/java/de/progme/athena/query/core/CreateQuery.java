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
import java.util.HashMap;
import java.util.List;

/**
 * Created by Marvin Erkes on 11.08.2015.
 *
 * Represents a SQL CREATE query.
 */
public class CreateQuery implements Query {

    private String table;

    private boolean createNotExists = false;

    private List<String> values = new ArrayList<>();

    private HashMap<String, List<String>> options = new HashMap<>();

    private List<String> primaryKeys;

    public CreateQuery(Builder builder) {

        this.table = builder.table;
        this.createNotExists = builder.createNotExists;
        this.primaryKeys = builder.primaryKeys;
        this.values = builder.values;
        this.options = builder.options;
    }

    @Override
    public String sql() {

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append((createNotExists) ? "IF NOT EXISTS " : "").append(table).append(" (");

        for (int i = 0; i < values.size(); i++) {
            if(i < values.size() - 1) {
                sb.append(values.get(i)).append(" ").append(String.join(" ", options.get(values.get(i)))).append(",");
            } else {
                sb.append(values.get(i)).append(" ").append(String.join(" ", options.get(values.get(i))));
            }
        }

        if(primaryKeys.size() > 0) {
            sb.append(",PRIMARY KEY (");
            for (int i = 0; i < primaryKeys.size(); i++) {
                if(i < primaryKeys.size() - 1) {
                    sb.append(primaryKeys.get(i)).append(",");
                } else {
                    sb.append(primaryKeys.get(i));
                }
            }
            sb.append(")");
        }

        sb.append(")").append(";");

        return sb.toString();
    }

    @Override
    public PreparedStatement prepareStatement(Connection connection) {

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return preparedStatement;
    }

    /**
     * Represents the builder for a select query.
     */
    public static class Builder {

        private String table;

        private boolean createNotExists = false;

        private List<String> values = new ArrayList<>();

        private HashMap<String, List<String>> options = new HashMap<>();

        private List<String> primaryKeys = new ArrayList<>();

        /**
         * Sets the table.
         *
         * @param table the table name.
         * @return the builder.
         */
        public Builder create(String table) {

            this.table = table;

            return this;
        }

        /**
         * Sets whether 'CREATE IF NOT EXISTS' is added or not.
         *
         * @param value true or false.
         * @return the builder.
         */
        public Builder ifNotExists(boolean value) {

            this.createNotExists = value;

            return this;
        }

        /**
         * Sets the column for the primary key.
         *
         * @param column the column name.
         * @return the builder.
         */
        public Builder primaryKey(String column) {

            this.primaryKeys.add(column);

            return this;
        }

        /**
         * Adds a value and it's options.
         *
         * @param value the value.
         * @param options the options.
         * @return the builder.
         */
        public Builder value(String value, String... options) {

            values.add(value);

            this.options.put(value, Arrays.asList(options));

            return this;
        }

        /**
         * Gets the finished CreateQuery.
         *
         * @return the CreateQuery.
         */
        public CreateQuery build() {

            return new CreateQuery(this);
        }
    }
}
