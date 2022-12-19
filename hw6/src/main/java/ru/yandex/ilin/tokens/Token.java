package ru.yandex.ilin.tokens;

import ru.yandex.ilin.visitors.TokenVisitor;

import java.util.Set;

public interface Token {
    void accept(TokenVisitor visitor);

    Set<Character> TOKENS = Set.of(')', '(', '*', '/', '-', '+');
}
