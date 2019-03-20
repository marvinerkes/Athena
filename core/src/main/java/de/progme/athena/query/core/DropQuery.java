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

/**
 * Created by Marvin Erkes on 11.08.2015.
 *
 * Represents a SQL DROP query.
 */
public class DropQuery implements Query {

    private String table;

    public DropQuery(Builder builder) {

        this.table = builder.table;
    }

    @Override
    public String sql() {

        return "DROP TABLE " + table + ";";
    }

    @Override
    public PreparedStatement prepareStatement(Connection connection) {

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statement;
    }

    /**
     * Represents the builder for a drop query.
     */
    public static class Builder {

        private String table;

        /**
         *Sets the table name that will be dropped.
         *
         * @param table the name of the table.
         * @return the builder.
         */
        public Builder drop(String table) {

            this.table = table;

            return this;
        }

        /**
         * Gets the finished DropQuery.
         *
         * @return the DropQuery.
         */
        public DropQuery build() {

            return new DropQuery(this);
        }
    }
}
