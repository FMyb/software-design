package ru.yandex.ilin.expression;


import ru.yandex.ilin.expression.exceptions.OverflowEEException;
import ru.yandex.ilin.expression.generic.Calculator;

import java.math.BigInteger;

public class Subtract<T extends Number> extends BaseOperation<T> {
    public Subtract(MyExpression<T> firstArgument, MyExpression<T> secondArgument, Calculator<T> calculator) {
        super(firstArgument, secondArgument, "-", calculator.sub());
    }

    public static int checkCorrect(int x, int y) {
        if (y < 0 && Integer.MAX_VALUE + y < x || y > 0 && Integer.MIN_VALUE + y > x) {
            throw new OverflowEEException("overflow");
        }
        return x - y;
    }
}
