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

package de.progme.athena;

import de.progme.athena.async.AthenaDispatcher;
import de.progme.athena.async.DispatcherConsumer;
import de.progme.athena.db.AthenaConnection;
import de.progme.athena.db.DBResult;
import de.progme.athena.db.Type;
import de.progme.athena.db.serialization.SerializationManager;
import de.progme.athena.db.settings.AthenaSettings;
import de.progme.athena.query.CustomQuery;
import de.progme.athena.query.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Future;

/**
 * Created by Marvin Erkes on 11.08.2015.
 *
 * This class has all API functions that Athena supports.
 */
public class Athena {

    /**
     * The settings.
     */
    private final AthenaSettings settings;

    /**
     * The connection.
     */
    private AthenaConnection connection;

    /**
     * The serialization manager for working with classes.
     */
    private final SerializationManager serializationManager;

    /**
     * The dispatcher for async executions.
     */
    private final AthenaDispatcher athenaDispatcher;

    /**
     * Create a new Athena object.
     *
     * @param settings the connection settings.
     */
    public Athena(AthenaSettings settings) {

        this.settings = settings;
        this.serializationManager = new SerializationManager(this);
        this.athenaDispatcher = new AthenaDispatcher();
    }

    /**
     * Connects with the sql server or database.
     */
    public void connect() {

        connection = new AthenaConnection(settings);
    }

    /**
     * Closes Athena and the async dispatcher.
     */
    public void close() {

        athenaDispatcher.close();
        connection.close();
    }

    /**
     * Dispatches an operation async with a callback.
     *
     * @param consumer the DispatcherConsumer callback.
     */
    public void dispatch(DispatcherConsumer consumer) {

        athenaDispatcher.dispatch(consumer);
    }

    /**
     * Submits an operation async with a callback and gets the operation future.
     *
     * @param consumer the DispatcherConsumer callback.
     * @return the Future of the operation.
     */
    public Future<?> submit(DispatcherConsumer consumer) {

        return athenaDispatcher.submit(consumer);
    }

    /**
     * Executes a SQL raw query.
     *
     * @param query the SQL raw query.
     */
    public boolean execute(String query) {

        return execute(new CustomQuery(query));
    }

    /**
     * Executes a Query as prepared SQL statement.
     *
     * If an exception occurs or the connection/query timeout is reached, it returns false.
     *
     * @param query the Query.
     */
    public boolean execute(Query query) {

        try (Connection con = connection.getConnection()) {
            PreparedStatement preparedStatement = query.prepareStatement(con);
            preparedStatement.execute();
            preparedStatement.close();

            return true;
        } catch (SQLException e) {
            if(settings.printExceptions()) {
                e.printStackTrace();
            }

            return false;
        }
    }

    /**
     * Queries a prepared statement and returns the result as DBResult.
     *
     * If an exception occurs or the connection/query timeout is reached, the DBResult returned is null.
     *
     * @param query the query.
     * @return the result as DBResult.
     */
    public DBResult query(Query query) {

        try (Connection con = connection.getConnection()) {
            PreparedStatement preparedStatement = query.prepareStatement(con);
            ResultSet resultSet = preparedStatement.executeQuery();

            return new DBResult(resultSet, preparedStatement);
        } catch (SQLException e) {
            if(settings.printExceptions()) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * Queries an SQL string query and returns the result as DBResult.
     *
     * @param query the SQL string query.
     * @return the result as DBResult.
     */
    public DBResult query(String query) {

        return query(new CustomQuery(query));
    }

    /**
     * Gets the database type of the Athena instance.
     *
     * @return the type.
     */
    public Type type() {

        return settings.type();
    }

    /**
     * Gets the serialization manager.
     *
     * @return the SerializationManager.
     */
    public SerializationManager serializationManager() {

        return serializationManager;
    }

    /**
     * Returns whether Athena and the backend connection pool is connected or not.
     *
     * @return True if connected, otherwise false.
     */
    public boolean isConnected() {

        return connection.isConnected();
    }
}
