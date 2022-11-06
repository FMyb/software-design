package ru.akirakozov.sd.refactoring.model;

/**
 * @author Yaroslav Ilin
 */
public class Product {
    private final int id;
    private final String name;
    private final long price;

    public Product(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, long price) {
        this.id = 0;
        this.name = name;
        this.price = price;
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public long price() {
        return price;
    }
}
