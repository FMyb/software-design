package ru.yandex.ilin.expression.parser;

import ru.yandex.ilin.expression.TripleExpression;
import ru.yandex.ilin.expression.Variable;
import ru.yandex.ilin.expression.exceptions.DBZEEException;
import ru.yandex.ilin.expression.exceptions.OverflowEEException;

/**
 * @author Yaroslav Ilin
 */
public class CheckedNegate implements TripleExpression {
    Variable y;

    public CheckedNegate(Variable y) {
        this.y = y;
    }


    @Override
    public int evaluate(int x, int y, int z) throws OverflowEEException {
        if (y == Integer.MIN_VALUE) {
            throw new OverflowEEException("overflow");
        }
        return -y;
    }
}
