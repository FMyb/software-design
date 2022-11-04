package ru.akirakozov.sd.refactoring;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

/**
 * @author Yaroslav Ilin
 */
public abstract class ProductTest {
    private Thread mainThread;
    protected SqliteConnector sqliteConnector;

    @Before
    public void setUp() throws InterruptedException {
        sqliteConnector = new SqliteConnector();
        mainThread = new Thread(() -> {
            try {
                Main.main(new String[]{});
            } catch (Exception ignore) {
            }
        });
        mainThread.start();
        Thread.sleep(1000);
        try (final PreparedStatement st = sqliteConnector.connect().prepareStatement("DELETE FROM PRODUCT;")) {
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() throws SQLException {
        sqliteConnector.reset();
        mainThread.interrupt();
    }

    static class SqliteConnector {
        private Connection connection;

        public synchronized Connection connect() throws SQLException {
            if (connection == null || !connection.isValid(1)) {
                connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            }
            return connection;
        }

        public void reset() throws SQLException {
            connection.close();
        }
    }
}
