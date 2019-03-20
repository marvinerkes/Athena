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

package de.progme.athena.db;

/**
 * Created by Marvin Erkes on 29.09.2015.
 *
 * Represents an enum for the supported database types and their driver names.
 */
public enum Type {

    MYSQL("com.mysql.cj.jdbc.Driver"), SQLITE("org.sqlite.JDBC");

    /**
     * The full driver name.
     */
    private String driver;

    /**
     * A new Type with a full name driver name.
     *
     * @param driver the full driver name.
     */
    Type(String driver) {

        this.driver = driver;
    }

    /**
     * Gets the full driver name.
     *
     * @return the driver name.
     */
    public String getDriver() {

        return driver;
    }
}
