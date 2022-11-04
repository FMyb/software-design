package ru.akirakozov.sd.refactoring;

import ru.akirakozov.sd.refactoring.connector.Connector;
import ru.akirakozov.sd.refactoring.connector.impl.SqliteConnector;
import ru.akirakozov.sd.refactoring.repository.impl.SqliteProductStorage;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Connector connector = new SqliteConnector("jdbc:sqlite:main.db");
        ProductService productService = new ProductService(
                connector,
                8081,
                new SqliteProductStorage(connector)
        );
        productService.initDatabase();
        productService.start();
        productService.stop();
    }
}
