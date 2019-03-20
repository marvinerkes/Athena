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
 * Represents a condition for a query with a column, an operator and a value.
 */
public class Condition {

    /**
     * The column name.
     */
    private final String column;

    /**
     * The operator to use.
     */
    private final Operator operator;

    /**
     * The value for the condition.
     */
    private final String value;

    /**
     * Creates a new condition with the given values.
     *
     * @param column the column name.
     * @param operator the operator.
     * @param value the value.
     */
    public Condition(String column, Operator operator, String value) {

        this.column = column;
        this.operator = operator;
        this.value = value;
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
     * Gets the operator.
     *
     * @return the operator.
     */
    public Operator operator() {

        return operator;
    }

    /**
     * Gets the value.
     *
     * @return the value.
     */
    public String value() {

        return value;
    }

    /**
     * Represents an operator like '=', '>', '!=' etc.
     */
    public enum Operator {

        EQUAL("="),
        GREATER(">"),
        LESS("<"),
        NOT_EQUAL("!="),
        GREATER_EQUAL(">="),
        LESS_EQUAL("<=");

        private String sql;

        Operator(String sql) {

            this.sql = sql;
        }

        /**
         * Gets the SQL string from the operator name.
         *
         * @return the SQL string.
         */
        public String sql() {

            return sql;
        }
    }
}
