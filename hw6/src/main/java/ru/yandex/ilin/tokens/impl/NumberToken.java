package ru.yandex.ilin.tokens.impl;

import ru.yandex.ilin.tokens.Token;
import ru.yandex.ilin.visitors.TokenVisitor;

public class NumberToken extends AbstractToken implements Token {
    private final int number;

    public NumberToken(int number) {
        super(String.valueOf(number));
        this.number = number;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    public int number() {
        return number;
    }
}
