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

/**
 * Created by Marvin Erkes on 26.08.2015.
 *
 * Defines the basic skeleton of a query class.
 */
public interface Query {

    /**
     * Gets the intern query as a raw SQL query string.
     *
     * @return the raw SQL query string.
     */
    String sql();

    /**
     * Gets the prepared statement from the query.
     *
     * @param connection the connection.
     * @return the prepared statement.
     */
    PreparedStatement prepareStatement(Connection connection);
}
