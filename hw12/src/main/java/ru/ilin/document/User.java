package ru.ilin.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("user")
public class User {
    @Id
    private String id;
    private String name;
    private Currency currency;

    public enum Currency {
        RUB, USD, EUR
    }

    public User(String id, String name, Currency currency) {
        this.id = id;
        this.name = name;
        this.currency = currency;
    }

    public User() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Currency getCurrency() {
        return currency;
    }
}
