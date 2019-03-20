package de.progme.athena.db;

/**
 * Created by Marvin Erkes on 23.11.2015.
 *
 * Represents an aggregate function with the type, column and alternatively an AS statement.
 */
public class Function {

    private Type type;

    private String column;

    private String as;

    /**
     * Creates a new aggregate function with the given type, column and AS statement.
     *
     * @param type the type.
     * @param column the column to perform the function on.
     * @param as the AS statement.
     */
    public Function(Type type, String column, String as) {

        this.type = type;
        this.column = column;
        this.as = as;
    }

    /**
     * Creates a new aggregate function with the given type, column and no AS statement.
     *
     * @param type the type.
     * @param column the column to perform the function on.
     */
    public Function(Type type, String column) {

        this(type, column, null);
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
     * Gets the column.
     *
     * @return the column to perform the function on.
     */
    public String column() {

        return column;
    }

    /**
     * Gets the AS statement.
     * Is null when no AS statement has been given.
     *
     * @return the AS statement.
     */
    public String as() {

        return as;
    }

    /**
     * Represents a aggregate function.
     */
    public enum Type {

        SUM("SUM"), AVG("AVG"), MIN("MIN"), MAX("MAX"), COUNT("COUNT");

        private String sql;

        Type(String sql) {

            this.sql = sql;
        }

        /**
         * Gets the SQL name from the type.
         *
         * @return the sql name.
         */
        public String sql() {

            return sql;
        }
    }
}
