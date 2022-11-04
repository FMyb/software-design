package ru.akirakozov.sd.refactoring.repository;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Yaroslav Ilin
 */
public interface Connector {
    Connection connect() throws SQLException;

    void reset() throws SQLException;
}
