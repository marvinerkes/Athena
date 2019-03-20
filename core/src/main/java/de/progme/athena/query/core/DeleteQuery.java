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
import java.util.Map;

/**
 * Created by Marvin Erkes on 28.09.2015.
 *
 * Represents a SQL DELETE query.
 */
public class DeleteQuery implements Query {

    private String table;

    private LinkedHashMap<String, String> wheres = new LinkedHashMap<>();

    private List<String> whereOperators = new ArrayList<>();

    public DeleteQuery(Builder builder) {

        this.table = builder.table;
        this.wheres = builder.wheres;
        this.whereOperators = builder.whereOperators;
    }

    @Override
    public String sql() {

        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM ").append(table);

        //TODO: Improve
        if(wheres.size() > 0) {
            sb.append(" WHERE ");

            int pos = 0;
            for (Map.Entry<String, String> entry : wheres.entrySet()) {
                sb.append(entry.getKey()).append(whereOperators.get(pos)).append("?").append(((wheres.size() > 1 && pos < wheres.size() - 1) ? " AND " : ""));
                pos++;
            }
        }

        sb.append(";");

        return sb.toString();
    }

    @Override
    public PreparedStatement prepareStatement(Connection connection) {

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql());
            List<String> wheresList = new ArrayList<>(wheres.values());
            for (int i = 0; i < wheresList.size(); i++) {
                preparedStatement.setObject(i + 1, wheresList.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return preparedStatement;
    }

    /**
     * Represents the builder for a delete query.
     */
    public static class Builder {

        private String table;

        private LinkedHashMap<String, String> wheres = new LinkedHashMap<>();

        private List<String> whereOperators = new ArrayList<>();

        /**
         * Sets the table name from which table data will be deleted.
         *
         * @param table the table name.
         * @return the builder.
         */
        public Builder from(String table) {

            this.table = table;

            return this;
        }

        /**
         * Adds a where clause with the given condition.
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
         * Gets the finished DeleteQuery.
         *
         * @return the DeleteQuery.
         */
        public DeleteQuery build() {

            return new DeleteQuery(this);
        }
    }
}
