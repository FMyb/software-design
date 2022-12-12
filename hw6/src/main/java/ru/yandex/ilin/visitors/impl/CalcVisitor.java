package ru.yandex.ilin.visitors.impl;

import ru.yandex.ilin.tokens.Token;
import ru.yandex.ilin.tokens.impl.Brace;
import ru.yandex.ilin.tokens.impl.NumberToken;
import ru.yandex.ilin.tokens.impl.Operation;
import ru.yandex.ilin.visitors.TokenVisitor;

import java.util.ArrayDeque;
import java.util.Deque;

public class CalcVisitor implements TokenVisitor<Integer> {
    private Deque<Integer> stack = new ArrayDeque<>();

    @Override
    public void visit(NumberToken token) {
        stack.add(token.number());
    }

    @Override
    public void visit(Brace token) {
    }

    @Override
    public void visit(Operation token) {
        if (stack.size() < 2) {
            throw new IllegalStateException("expected 2 arguments for operation " + token.toString());
        }
        int right = stack.removeLast();
        int left = stack.removeLast();
        stack.add(
            switch (token.toString()) {
                case "+" -> left + right;
                case "-" -> left - right;
                case "*" -> left * right;
                case "/" -> left / right;
                default -> throw new IllegalStateException("Unexpected value: " + token);
            }
        );
    }

    @Override
    public Integer result() {
        if (stack.size() < 1) {
            return null;
        }
        if (stack.size() > 1) {
            throw new IllegalStateException("operations not calculated successfully");
        }
        return stack.getLast();
    }
}
