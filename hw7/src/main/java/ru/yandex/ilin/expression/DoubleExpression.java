package ru.yandex.ilin.expression;

public strictfp interface DoubleExpression extends ToMiniString {
    double evaluate(double x);
}
