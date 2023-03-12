package ru.ilin.dto;

public class AddProductRequest {
    private String name;
    private long price;

    public AddProductRequest(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public AddProductRequest() {
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
}
