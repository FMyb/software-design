package ru.akirakozov.sd.refactoring.repository.impl;

import ru.akirakozov.sd.refactoring.connector.Connector;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.repository.ProductStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yaroslav Ilin
 */
public class SqliteProductStorage implements ProductStorage {
    private final Connector connector;

    public SqliteProductStorage(Connector connector) {
        this.connector = connector;
    }

    @Override
    public List<Product> list() {
        try (PreparedStatement ps = connector.connect().prepareStatement("SELECT * FROM PRODUCT;")) {
            ResultSet rs = ps.executeQuery();
            List<Product> result = new ArrayList<>();
            while (rs.next()) {
                result.add(fromResultSet(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Product product) {
        try (PreparedStatement ps = connector.connect().prepareStatement(
                "INSERT INTO PRODUCT (NAME, PRICE) VALUES (?, ?);"
        )) {
            int index = 0;
            ps.setString(++index, product.name());
            ps.setLong(++index, product.price());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product max() {
        try (PreparedStatement ps = connector.connect().prepareStatement(
                "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1;"
        )) {
            ResultSet rs = ps.executeQuery();
            return fromResultSet(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product min() {
        try (PreparedStatement ps = connector.connect().prepareStatement(
                "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1;"
        )) {
            ResultSet rs = ps.executeQuery();
            return fromResultSet(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int sum() {
        try (PreparedStatement ps = connector.connect().prepareStatement(
                "SELECT SUM(price) FROM PRODUCT;"
        )) {
            ResultSet rs = ps.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int count() {
        try (PreparedStatement ps = connector.connect().prepareStatement(
                "SELECT COUNT(*) FROM PRODUCT;"
        )) {
            ResultSet rs = ps.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Product fromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int price = resultSet.getInt("price");
        return new Product(id, name, price);
    }
}
