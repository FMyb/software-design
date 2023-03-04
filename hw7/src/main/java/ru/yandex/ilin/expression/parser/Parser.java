package ru.yandex.ilin.expression.parser;

import ru.yandex.ilin.expression.MyExpression;
import ru.yandex.ilin.expression.TripleExpression;
import ru.yandex.ilin.expression.exceptions.IllegalArgumentException;
import ru.yandex.ilin.expression.exceptions.IllegalNumberException;
import ru.yandex.ilin.expression.exceptions.IllegalOperationException;
import ru.yandex.ilin.expression.exceptions.IllegalSymbolException;
import ru.yandex.ilin.expression.generic.Calculator;

/**
 * @author Yaroslav Ilin
 */

public interface Parser<T extends Number> {
    TripleExpression parse(String expression) throws IllegalSymbolException, IllegalOperationException, IllegalArgumentException, IllegalNumberException;

    MyExpression<T> parse(String expression, Calculator<T> calculator) throws IllegalSymbolException, IllegalOperationException, IllegalArgumentException, IllegalNumberException;
}
