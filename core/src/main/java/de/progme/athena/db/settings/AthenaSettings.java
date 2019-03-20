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

package de.progme.athena.db.settings;

import de.progme.athena.db.Type;

/**
 * Created by Marvin Erkes on 11.08.2015.
 *
 * Represents a class for Athena to configure the connection pool and internal options.
 */
public class AthenaSettings {

    private String host = "localhost";

    private int port = 3306;

    private String user = "root";

    private String password = "";

    private String database = "db";

    private int poolSize = 10;

    private Type type = Type.MYSQL;

    private String poolName;

    private boolean printExceptions = false;

    private long queryTimeout = 2000;

    public AthenaSettings() {

    }

    public AthenaSettings(String host, int port, String user, String password, String database, int poolSize, Type type, String poolName, boolean printExceptions, long queryTimeout) {

        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
        this.poolSize = poolSize;
        this.type = type;
        this.poolName = poolName;
        this.printExceptions = printExceptions;
        this.queryTimeout = queryTimeout;
    }

    public String host() {

        return host;
    }

    public int port() {

        return port;
    }

    public String user() {

        return user;
    }

    public String password() {

        return password;
    }

    public String database() {

        return database;
    }

    public int poolSize() {

        return poolSize;
    }

    public Type type() {

        return type;
    }

    public String poolName() {

        return poolName;
    }

    public boolean printExceptions() {

        return printExceptions;
    }

    public long getQueryTimeout() {

        return queryTimeout;
    }

    /**
     * The builder for AthenaSettings.
     */
    public static class Builder {

        private AthenaSettings settings = new AthenaSettings();

        /**
         * Sets the host.
         *
         * @param host the host.
         * @return the Builder.
         */
        public Builder host(String host) {

            settings.host = host;

            return this;
        }

        /**
         * Sets the port of the server.
         *
         * @param port the port.
         * @return the Builder.
         */
        public Builder port(int port) {

            settings.port = port;

            return this;
        }

        /**
         * Sets the username from the server.
         *
         * @param user the username.
         * @return the Builder.
         */
        public Builder user(String user) {

            settings.user = user;

            return this;
        }

        /**
         * Sets the password from the server.
         *
         * @param password the password.
         * @return the Builder.
         */
        public Builder password(String password) {

            settings.password = password;

            return this;
        }

        /**
         * Sets the database name or the path for sqlite.
         *
         * @param database the database name or the path.
         * @return the Builder.
         */
        public Builder database(String database) {

            settings.database = database;

            return this;
        }

        /**
         * Sets the pool size from the connection pool.
         *
         * @param size the size.
         * @return the Builder.
         */
        public Builder poolSize(int size) {

            settings.poolSize = size;

            return this;
        }

        /**
         * Sets the type of the Athena instance.
         *
         * @param type the type.
         * @return the Builder.
         */
        public Builder type(Type type) {

            settings.type = type;

            return this;
        }

        /**
         * Sets the pool name from the connection pool.
         *
         * @param name the name.
         * @return the Builder.
         */
        public Builder poolName(String name) {

            settings.poolName = name;

            return this;
        }

        /**
         * Sets if exceptions should be thrown.
         *
         * @param value true or false.
         * @return the Builder.
         */
        public Builder printExceptions(boolean value) {

            settings.printExceptions = value;

            return this;
        }

        /**
         * Sets the query timeout value.
         *
         * @param queryTimeoutMs the query timeout.
         * @return the builder.
         */
        public Builder queryTimeout(long queryTimeoutMs) {

            settings.queryTimeout = queryTimeoutMs;

            return this;
        }

        /**
         * Gets the finished AthenaSettings instance.
         *
         * @return the AthenaSettings instance.
         */
        public AthenaSettings build() {

            return settings;
        }
    }
}
