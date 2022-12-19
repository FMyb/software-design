package ru.yandex.ilin.tokens.impl;

import ru.yandex.ilin.tokens.Token;

import java.util.Objects;

public abstract class AbstractToken implements Token {
    private final String symbol;

    protected AbstractToken(String symbol) {this.symbol = symbol;}

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AbstractToken && Objects.equals(o.toString(), toString());
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }
}
