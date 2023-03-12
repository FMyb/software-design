package ru.ilin.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("product")
public class Product {
    @Id
    private String id;
    private String name;
    private long price;

    public Product(String id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Product withPrice(long price) {
        this.price = price;
        return this;
    }
}
