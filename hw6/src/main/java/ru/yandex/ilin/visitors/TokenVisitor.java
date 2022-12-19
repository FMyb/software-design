package ru.yandex.ilin.visitors;

import ru.yandex.ilin.tokens.Token;
import ru.yandex.ilin.tokens.impl.Brace;
import ru.yandex.ilin.tokens.impl.NumberToken;
import ru.yandex.ilin.tokens.impl.Operation;

import java.util.List;

public interface TokenVisitor<T> {
    void visit(NumberToken token);
    void visit(Brace token);
    void visit(Operation token);
    T result();
}
