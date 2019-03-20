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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Marvin Erkes on 11.08.2015.
 *
 * Represents a class to hold the results from a query.
 */
public class DBResult {

    /**
     * List of all rows.
     */
    private List<DBRow> rows = new ArrayList<>();

    /**
     * Creates an instance of DBResult.
     *
     * @param resultSet a ResultSet from a prepareStatement.
     * @param preparedStatement a PreparedStatement from a prepareStatement.
     */
    public DBResult(ResultSet resultSet, PreparedStatement preparedStatement) {

        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            List<String> columns = new ArrayList<>(columnCount);
            columnCount += 1;
            for (int i = 1; i < columnCount; i++) {
                columns.add(resultSetMetaData.getColumnName(i));
            }

            while (resultSet.next()) {
                DBRow row = new DBRow();

                for (String column : columns) {
                    row.add(column, resultSet.getObject(column));
                }

                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the size of the row list.
     *
     * @return the size.
     */
    public int size() {

        return rows.size();
    }

    /**
     * Gets all rows from the prepareStatement.
     *
     * @return a list with all rows as DBRow objects.
     */
    public List<DBRow> rows() {

        return Collections.unmodifiableList(rows);
    }

    /**
     * Gets a row from the given index.
     *
     * @param index the index.
     * @return the DBRow instance.
     */
    public DBRow row(int index) {

        return rows.get(index);
    }
}
