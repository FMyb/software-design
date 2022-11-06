package ru.akirakozov.sd.refactoring.connector.impl;

import ru.akirakozov.sd.refactoring.connector.Connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnector implements Connector {
    private Connection connection;
    private final String sqliteAddress;

    public SqliteConnector(String sqliteAddress) {
        this.sqliteAddress = sqliteAddress;
    }

    public synchronized Connection connect() throws SQLException {
        if (connection == null || !connection.isValid(1)) {
            connection = DriverManager.getConnection(sqliteAddress);
        }
        return connection;
    }

    public void reset() throws SQLException {
        connection.close();
    }
}