package ru.yandex.ilin.visitors.impl;

import ru.yandex.ilin.tokens.Token;
import ru.yandex.ilin.tokens.impl.Brace;
import ru.yandex.ilin.tokens.impl.NumberToken;
import ru.yandex.ilin.tokens.impl.Operation;
import ru.yandex.ilin.visitors.TokenVisitor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ParserVisitor implements TokenVisitor<List<Token>> {
    private final Deque<Token> stack = new ArrayDeque<>();
    private final List<Token> result = new ArrayList<>();


    @Override
    public void visit(NumberToken token) {
        result.add(token);
    }

    @Override
    public void visit(Brace token) {
        switch (token.toString()) {
            case "(" -> stack.add(token);
            case ")" -> {
                if (stack.isEmpty()) {
                    throw new IllegalStateException("expected open bracket");
                }
                Token cur = stack.removeLast();
                while (!"(".equals(cur.toString())) {
                    result.add(cur);
                    if (stack.isEmpty()) {
                        throw new IllegalStateException("expected open bracket");
                    }
                    cur = stack.removeLast();
                }
            }
        }
    }

    @Override
    public void visit(Operation token) {
        if (!stack.isEmpty()) {
            Token cur = stack.getLast();
            while (cur instanceof Operation && ((Operation) cur).priority() >= token.priority()) {
                result.add(stack.removeLast());
                if (stack.isEmpty()) break;
                cur = stack.getLast();
            }
        }
        stack.add(token);
    }

    @Override
    public List<Token> result() {
        while (!stack.isEmpty()) {
            result.add(stack.removeLast());
        }
        return result;
    }
}
