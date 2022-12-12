package ru.yandex.ilin.tokenizer.impl;

import ru.yandex.ilin.tokenizer.Tokenizer;
import ru.yandex.ilin.tokens.Token;
import ru.yandex.ilin.tokens.impl.Brace;
import ru.yandex.ilin.tokens.impl.NumberToken;
import ru.yandex.ilin.tokens.impl.Operation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TokenizerImpl implements Tokenizer {
    private State currentState = State.START;
    private final BufferedReader reader;
    private Character currentSymbol;
    private Integer currentNumber;

    public TokenizerImpl(InputStream inputStream) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        currentSymbol = nextSymbol();
    }

    @Override
    public Optional<Token> next() {
        switch (currentState) {
            case START -> {
                if (currentSymbol == null) {
                    currentState = State.END;
                    return Optional.empty();
                }
                if (Token.TOKENS.contains(currentSymbol)) {
                    Token token = toToken(currentSymbol);
                    currentSymbol = nextSymbol();
                    return Optional.of(token);
                }
                if (Character.isDigit(currentSymbol)) {
                    currentState = State.NUMBER;
                    currentNumber = Character.digit(currentSymbol, 10);
                    currentSymbol = nextSymbol();
                    return next();
                } else {
                    currentState = State.ERROR;
                    throw new IllegalArgumentException("not supported symbol" + currentSymbol);
                }
            }
            case NUMBER -> {
                if (currentSymbol != null && Character.isDigit(currentSymbol)) {
                    currentNumber = currentNumber * 10 + Character.digit(currentSymbol, 10);
                    currentSymbol = nextSymbol();
                    next();
                } else {
                    currentState = State.START;
                    int number = currentNumber;
                    currentNumber = null;
                    return Optional.of(new NumberToken(number));
                }
            }
            case ERROR -> throw new IllegalArgumentException("not supported symbol" + currentSymbol);
            case END -> {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Token> all() {
        List<Token> result = new ArrayList<>();
        Optional<Token> cur = next();
        while (cur.isPresent()) {
            result.add(cur.get());
            cur = next();
        }
        return result;
    }

    private Character nextSymbol() {
        char cur = ' ';
        while (Character.isWhitespace(cur)) {
            try {
                int x = reader.read();
                if (x == -1) {
                    return null;
                } else {
                    cur = (char) x;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return cur;
    }

    private Token toToken(char currentSymbol) {
        return switch (currentSymbol) {
            case '(' -> Brace.openBrace();
            case ')' -> Brace.closeBrace();
            case '*' -> Operation.mulOperation();
            case '/' -> Operation.divOperation();
            case '-' -> Operation.subOperation();
            case '+' -> Operation.sumOperation();
            default -> throw new IllegalArgumentException("not supported symbol " + currentSymbol);
        };
    }

    private enum State {
        START,
        NUMBER,
        ERROR,
        END
    }
}
