package ru.yandex.ilin.expression;

import ru.yandex.ilin.expression.exceptions.DBZEEException;
import ru.yandex.ilin.expression.exceptions.OverflowEEException;

public interface TripleExpression extends ToMiniString {
    int evaluate(int x, int y, int z) throws OverflowEEException, DBZEEException;
}
