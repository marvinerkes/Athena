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

package de.progme.athena.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Marvin Erkes on 10.10.2015.
 *
 * Represents a custom query with a SQL query string as constructor parameter.
 */
public class CustomQuery implements Query {

    /**
     * The SQL query string.
     */
    private String sql;

    /**
     * Create a new CustomQuery with the given SQL query string.
     *
     * @param sql the SQL query string.
     */
    public CustomQuery(String sql) {

        this.sql = sql;
    }

    @Override
    public String sql() {

        return sql;
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
}
