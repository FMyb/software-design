package ru.akirakozov.sd.refactoring.repository;

import ru.akirakozov.sd.refactoring.model.Product;

import java.util.List;

/**
 * @author Yaroslav Ilin
 */
public interface ProductStorage {
    List<Product> list();

    void create(Product product);

    Product max();

    Product min();

    int sum();

    int count();
}
