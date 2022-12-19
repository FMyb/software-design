package ru.yandex.ilin.tokens.impl;

import ru.yandex.ilin.tokens.Token;
import ru.yandex.ilin.visitors.TokenVisitor;

public class Brace extends AbstractToken implements Token {
    public Brace(String symbol) {
        super(symbol);
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    public static Brace openBrace() {
        return new Brace("(");
    }

    public static Brace closeBrace() {
        return new Brace(")");
    }
}
