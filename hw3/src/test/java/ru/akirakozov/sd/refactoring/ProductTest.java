package ru.akirakozov.sd.refactoring;

import org.junit.After;
import org.junit.Before;
import ru.akirakozov.sd.refactoring.connector.impl.SqliteConnector;
import ru.akirakozov.sd.refactoring.repository.ProductStorage;
import ru.akirakozov.sd.refactoring.repository.impl.SqliteProductStorage;

import java.sql.*;

/**
 * @author Yaroslav Ilin
 */
public class ProductTest {
    protected SqliteConnector sqliteConnector;
    protected ProductService productService;
    protected String serverHost = "localhost";
    protected int serverPort = 15234;
    protected String serverUrl = String.format("http://%s:%d", serverHost, serverPort);
    protected ProductStorage productStorage;

    @Before
    public void setUpService() throws Exception {
        sqliteConnector = new SqliteConnector("jdbc:sqlite:test.db");
        productStorage = new SqliteProductStorage(sqliteConnector);
        productService = new ProductService(sqliteConnector, serverPort, productStorage);
        productService.initDatabase();
        productService.start();

        try (final PreparedStatement st = sqliteConnector.connect().prepareStatement("DELETE FROM PRODUCT;")) {
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() throws Exception {
        productService.stop();
    }

    public String serverUrl() {
        return serverUrl;
    }
}
