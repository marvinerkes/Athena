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

package de.progme.athena.db.serialization.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Timestamp;

/**
 * Created by Marvin Erkes on 10.10.2015.
 *
 * The annotation to mark a field as a column.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    /**
     * Gets the column name in the database table.
     *
     * @return the column name in the database table.
     */
    String name() default "";

    /**
     * Gets the options from the database column.
     *
     * @return the column options.
     */
    Option[] options() default Option.EMPTY;

    /**
     * Represents an option for a column.
     */
    enum Option {
        EMPTY(""),
        NOT_NULL("NOT NULL"),
        AUTO_INCREMENT("AUTO_INCREMENT"),
        UNIQUE("UNIQUE"),
        PRIMARY_KEY("PRIMARY_KEY");

        private String sql;

        Option(String sql) {

            this.sql = sql;
        }

        public boolean isOption(Column.Option[] options) {

            for (Option option : options) {
                if (option == this) {
                    return true;
                }
            }

            return false;
        }

        /**
         * Gets the SQL string from the option type.
         *
         * @return the SQL string.
         */
        public String sql() {

            return sql;
        }
    }

    /**
     * Represents an enum with valid data types and the SQL representation.
     * Used only internally.
     */
    enum Type {
        INTEGER("INTEGER", int.class, Integer.class),
        STRING("VARCHAR(255)", String.class),
        BOOLEAN("BOOLEAN", boolean.class, Boolean.class),
        LONG("BIGINT", Long.class, long.class),
        DOUBLE("DOUBLE", double.class, Double.class),
        FLOAT("FLOAT", Float.class, float.class),
        BYTE("TINYINT", Byte.class, byte.class),
        SHORT("SMALLINT", Short.class, short.class),
        CHAR("CHAR", char.class),
        DATETIME("DATETIME", Timestamp.class),
        UNKNOWN("UNKNOWN");

        private String sqlName;

        private Class<?>[] javaClasses;

        Type(String sqlName, Class<?>... javaClasses) {

            this.sqlName = sqlName;
            this.javaClasses = javaClasses;
        }

        /**
         * Gets a type from a data type class name.
         *
         * @param clazz the class.
         * @return the type.
         */
        public static Type typeFromClass(Class<?> clazz) {

            for(Type type : Type.values()) {
                for(Class<?> javaClass : type.javaClasses) {
                    if(javaClass == clazz) {
                        return type;
                    }
                }
            }

            return Type.UNKNOWN;
        }

        /**
         * Gets the SQL string from the type.
         *
         * @return the SQL string from the type.
         */
        public String sqlName() {

            return sqlName;
        }

        /**
         * Gets the corresponding classes.
         *
         * @return the classes.
         */
        public Class<?>[] javaClasses() {

            return javaClasses;
        }
    }
}