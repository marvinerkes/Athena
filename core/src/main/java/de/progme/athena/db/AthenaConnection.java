package de.progme.athena.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.progme.athena.db.settings.AthenaSettings;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Marvin Erkes on 29.09.2015.
 *
 * Represents a class to get a connection from the connection pool.
 */
public class AthenaConnection {

    /**
     * The database type.
     */
    private Type type;

    /**
     * The settings for the Athena instance.
     */
    private AthenaSettings settings;

    /**
     * The data source.
     */
    private HikariDataSource dataSource;

    /**
     * Creates a new AthenaConnection instance with the settings.
     *
     * @param settings the settings.
     */
    public AthenaConnection(AthenaSettings settings) {

        this.type = settings.type();
        this.settings = settings;

        setup();
    }

    /**
     * Prepares the connection pool.
     */
    private void setup() {

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(type.getDriver());
        if(type == Type.MYSQL) {
            config.setJdbcUrl("jdbc:mysql://" + settings.host() + ":" + settings.port() + "/" + settings.database() + "?serverTimezone=UTC");
        } else if(type == Type.SQLITE) {
            config.setJdbcUrl("jdbc:sqlite:" + settings.database());

            String path = settings.database().substring(0, settings.database().lastIndexOf("/"));

            File pathFile = new File(path);
            if(!pathFile.exists()) {
                if (!new File(path).mkdirs()) {
                    System.err.println("Error while creating path to database file! " + path);
                }
            }
        }
        config.setUsername(settings.user());
        config.setPassword(settings.password());
        config.setPoolName(settings.poolName());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(settings.poolSize());
        config.setConnectionTimeout(settings.getQueryTimeout());

        dataSource = new HikariDataSource(config);
    }

    /**
     * Gets a connection from the data source.
     *
     * @return the Connection.
     * @throws SQLException if a database access error occurs.
     */
    public Connection getConnection() throws SQLException {

        return dataSource.getConnection();
    }

    /**
     * Closes the data source and the connection pool.
     */
    public void close() {

        dataSource.close();
    }

    /**
     * Returns whether the data source is connected or not.
     *
     * @return True if the data source is not closed, otherwise false.
     */
    public boolean isConnected() {

        return !dataSource.isClosed();
    }
}
