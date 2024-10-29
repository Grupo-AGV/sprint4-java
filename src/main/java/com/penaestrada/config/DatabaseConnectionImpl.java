package com.penaestrada.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

class DatabaseConnectionImpl implements DatabaseConnection {

    private static DatabaseConnectionImpl instance;

    private static Connection connection;

    private final Logger logger = Logger.getLogger(DatabaseConnectionImpl.class.getName());

    private DatabaseConnectionImpl() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            logger.severe("driver do oracle nã foi localizado");
        }

        connection = DriverManager.getConnection(DatabaseConfig.getUrl(), DatabaseConfig.getUser(), DatabaseConfig.getPass());
    }

    synchronized static DatabaseConnectionImpl getInstance() throws SQLException{
        if (instance == null || connection.isClosed()) {
            instance = new DatabaseConnectionImpl();
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        connection.setAutoCommit(false);
        return connection;
    }
}
