package ru.yandex.ilin.tokens.impl;

import ru.yandex.ilin.tokens.Token;
import ru.yandex.ilin.visitors.TokenVisitor;

public class Operation extends AbstractToken implements Token{
    private final int priority;

    public Operation(String symbol, int priority) {
        super(symbol);
        this.priority = priority;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    public int priority() {
        return priority;
    }

    public static Operation sumOperation() {
        return new Operation("+", 0);
    }

    public static Operation subOperation() {
        return new Operation("-", 0);
    }

    public static Operation mulOperation() {
        return new Operation("*", 1);
    }

    public static Operation divOperation() {
        return new Operation("/", 1);
    }
}
