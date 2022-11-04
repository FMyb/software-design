package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.connector.impl.SqliteConnector;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Yaroslav Ilin
 */
public class ProductService {
    private final int serverPort;
    private final Server server;
    private final SqliteConnector sqliteConnector;

    public ProductService(SqliteConnector sqliteConnector, int serverPort) {
        this.sqliteConnector = sqliteConnector;
        this.serverPort = serverPort;
        this.server = new Server(serverPort);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet()), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet()), "/get-products");
        context.addServlet(new ServletHolder(new QueryServlet()), "/query");
    }

    public void initDatabase() {
        try (Connection c = sqliteConnector.connect()) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() throws Exception {
        server.start();
    }

    public void stop() throws Exception {
        try {
            sqliteConnector.reset();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        server.stop();
    }
}
