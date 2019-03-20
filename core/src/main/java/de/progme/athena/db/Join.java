package de.progme.athena.db;

/**
 * Created by Marvin Erkes on 13.11.2015.
 *
 * Represents a SQL JOIN statement with the table to join on, the column and a value or other column.
 */
public class Join {

    private Type type;

    private String joinTable;

    private String onColumn;

    private String onValue;

    /**
     * Creates a new SQL JOIN statement with the given values.
     *
     * @param type the type.
     * @param joinTable the table.
     * @param onColumn the column.
     * @param onValue the value.
     */
    public Join(Type type, String joinTable, String onColumn, String onValue) {

        this.type = type;
        this.joinTable = joinTable;
        this.onColumn = onColumn;
        this.onValue = onValue;
    }

    /**
     * Creates a new SQL JOIN statement with the given values and LEFT_JOIN as type.
     *
     * @param joinTable the table.
     * @param onColumn the column.
     * @param onValue the value.
     */
    public Join(String joinTable, String onColumn, String onValue) {

        this(Type.LEFT_JOIN, joinTable, onColumn, onValue);
    }

    /**
     * Gets the type of the join.
     *
     * @return the type.
     */
    public Type type() {

        return type;
    }

    /**
     * Gets the table.
     *
     * @return the table.
     */
    public String joinTable() {

        return joinTable;
    }

    /**
     * Gets the column.
     *
     * @return the column.
     */
    public String onColumn() {

        return onColumn;
    }

    /**
     * Gets the value or other column.
     *
     * @return the value/column.
     */
    public String onValue() {

        return onValue;
    }

    public enum  Type {

        LEFT_JOIN("LEFT JOIN"),
        RIGHT_JOIN("RIGHT JOIN");

        private String sql;

        Type(String sql) {

            this.sql = sql;
        }

        public String sql() {

            return sql;
        }
    }
}
