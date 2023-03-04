package ru.yandex.ilin.expression;

public interface ToMiniString {
    default String toMiniString() {
        return toString();
    }
}
