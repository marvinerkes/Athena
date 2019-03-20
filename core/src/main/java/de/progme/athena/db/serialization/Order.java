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

package de.progme.athena.db.serialization;

/**
 * Created by Marvin Erkes on 11.10.2015.
 *
 * Represents an order with the column name and the type ('DESC' or 'ASC').
 */
public class Order {

    private final String column;

    private final Type type;

    /**
     * Creates a new order with the given column and type.
     *
     * @param column the column name.
     * @param type the type of the order.
     */
    public Order(String column, Type type) {

        this.column = column;
        this.type = type;
    }

    /**
     * Gets the column name.
     *
     * @return the column name.
     */
    public String column() {

        return column;
    }

    /**
     * Gets the type.
     *
     * @return the type.
     */
    public Type type() {

        return type;
    }

    /**
     * Represents a SQL order type.
     */
    public enum Type {
        DESC,
        ASC
    }
}
