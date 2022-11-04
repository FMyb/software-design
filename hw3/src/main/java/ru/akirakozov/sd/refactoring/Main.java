package ru.akirakozov.sd.refactoring;

import ru.akirakozov.sd.refactoring.connector.impl.SqliteConnector;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        ProductService productService = new ProductService(new SqliteConnector("jdbc:sqlite:main.db"), 8081);
        productService.initDatabase();
        productService.start();
        productService.stop();
    }
}
