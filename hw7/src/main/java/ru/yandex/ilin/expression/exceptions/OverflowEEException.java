package ru.yandex.ilin.expression.exceptions;

/**
 * @author Yaroslav Ilin
 */
public class OverflowEEException extends EEException {

    public OverflowEEException(String message) {
        super("overflow on" + message);
    }
}
