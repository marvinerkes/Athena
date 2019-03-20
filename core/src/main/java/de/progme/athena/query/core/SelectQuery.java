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

import de.progme.athena.db.Function;
import de.progme.athena.db.Join;
import de.progme.athena.db.serialization.Condition;
import de.progme.athena.db.serialization.Order;
import de.progme.athena.query.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Marvin Erkes on 11.08.2015.
 *
 * Represents a SQL SELECT query.
 */
public class SelectQuery implements Query {

    private List<String> selects = new ArrayList<>();

    private List<Function> functions = new ArrayList<>();

    private String table;

    private List<Join> joins = new ArrayList<>();

    private LinkedHashMap<String, String> wheres = new LinkedHashMap<>();

    private List<String> operators = new ArrayList<>();

    private String orderBy = null;

    private String limit = null;

    public SelectQuery(Builder builder) {

        this.selects = builder.selects;
        this.functions = builder.functions;
        this.table = builder.table;
        this.wheres = builder.wheres;
        this.joins = builder.joins;
        this.operators = builder.operators;
        this.orderBy = builder.orderBy;
        this.limit = builder.limit;
    }

    @Override
    public String sql() {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for (int i = 0; i < selects.size(); i++) {
            if(i < selects.size() - 1) {
                sb.append(selects.get(i)).append(",");
            } else {
                sb.append(selects.get(i));
            }
        }

        if(functions.size() > 0) {
            sb.append(", ");
            for (int i = 0; i < functions.size(); i++) {
                Function function = functions.get(i);
                if (i < functions.size() - 1) {
                    sb.append(function.type().sql()).append("(").append(function.column()).append(")").append((function.as() != null) ? " AS " + function.as() : "").append(",");
                } else {
                    sb.append(function.type().sql()).append("(").append(function.column()).append(")").append((function.as() != null) ? " AS " + function.as() : "");
                }
            }
        }

        sb.append(" FROM ").append(table);

        if(joins.size() > 0) {
            for (int i = 0; i < joins.size(); i++) {
                Join join = joins.get(i);
                if(i < joins.size() - 1) {
                    sb.append(" ").append(join.type().sql()).append(" ").append(join.joinTable()).append(" ON ").append(join.onColumn()).append("=").append(join.onValue()).append(",");
                } else {
                    sb.append(" ").append(join.type().sql()).append(" ").append(join.joinTable()).append(" ON ").append(join.onColumn()).append("=").append(join.onValue());
                }
            }
        }

        //TODO: Improve
        if(wheres.size() > 0) {
            sb.append(" WHERE ");

            int pos = 0;
            for (String whereKey : wheres.keySet()) {
                sb.append(whereKey).append(operators.get(pos)).append("?").append(((wheres.size() > 1 && pos < wheres.size() - 1) ? " AND " : ""));
                pos++;
            }
        }

        if(orderBy != null) {
            sb.append(" ORDER BY ").append(orderBy);
        }

        if(limit != null) {
            sb.append(" LIMIT ").append(limit);
        }

        return sb.append(";").toString();
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
     * Represents the builder for a select query.
     */
    public static class Builder {

        private List<String> selects = new ArrayList<>();

        private List<Function> functions = new ArrayList<>();

        private String table;

        private List<Join> joins = new ArrayList<>();

        private LinkedHashMap<String, String> wheres = new LinkedHashMap<>();

        private List<String> operators = new ArrayList<>();

        private String orderBy = null;

        private String limit = null;

        /**
         * Adds the columns which should be selected.
         *
         * @param selects the columns.
         * @return the builder.
         */
        public Builder select(String... selects) {

            Collections.addAll(this.selects, selects);

            return this;
        }

        /**
         * Adds the column which should be selected.
         *
         * @param select the columns.
         * @return the builder.
         */
        public Builder select(String select) {

            this.selects.add(select);

            return this;
        }

        /**
         * Adds the functions to the query.
         *
         * @param function the functions.
         * @return the builder.
         */
        public Builder function(Function... function) {

            this.functions.addAll(Arrays.asList(function));

            return this;
        }

        /**
         * Sets the table from which the query will select.
         *
         * @param table the table name.
         * @return the builder.
         */
        public Builder from(String table) {

            this.table = table;

            return this;
        }

        /**
         * Adds the joins to the query.
         *
         * @param joins the joins.
         * @return the builder.
         */
        public Builder join(Join... joins) {

            this.joins.addAll(Arrays.asList(joins));

            return this;
        }

        /**
         * Adds a where statement with the given condition.
         *
         * @param condition the condition.
         * @return the builder.
         */
        public Builder where(Condition condition) {

            this.wheres.put(condition.column(), condition.value());
            this.operators.add(condition.operator().sql());

            return this;
        }

        /**
         * Adds the order statements.
         *
         * Keep in mind that this resets the old order statement from this builder.
         *
         * @param orders the orders.
         * @return the builder.
         */
        public Builder orderBy(Order... orders) {

            orderBy = "";
            for (int i = 0; i < orders.length; i++) {
                Order order = orders[i];

                if(i < orders.length - 1) {
                    this.orderBy += order.column() + " " + order.type().toString() + ",";
                } else {
                    this.orderBy += order.column() + " " + order.type().toString();
                }
            }

            return this;
        }

        /**
         * Sets the limit.
         *
         * @param limit the limit.
         * @return the builder.
         */
        public Builder limit(String limit) {

            this.limit = limit;

            return this;
        }

        /**
         * Sets the limit with an int.
         *
         * @param limit the limit as an int.
         * @return the builder.
         */
        public Builder limit(int limit) {

            return limit("" + limit);
        }

        /**
         * Gets the finished SelectQuery.
         *
         * @return the SelectQuery.
         */
        public SelectQuery build() {

            return new SelectQuery(this);
        }
    }
}
