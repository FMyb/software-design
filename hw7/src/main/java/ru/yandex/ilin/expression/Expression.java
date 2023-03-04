package ru.yandex.ilin.expression;

public interface Expression extends ToMiniString {
    int evaluate(int x);
}
