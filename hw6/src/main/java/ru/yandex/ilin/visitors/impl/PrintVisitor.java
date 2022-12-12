package ru.yandex.ilin.visitors.impl;

import ru.yandex.ilin.tokens.impl.Brace;
import ru.yandex.ilin.tokens.impl.NumberToken;
import ru.yandex.ilin.tokens.impl.Operation;
import ru.yandex.ilin.visitors.TokenVisitor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class PrintVisitor implements TokenVisitor<String> {
    private final BufferedWriter writer;
    private final StringBuilder result = new StringBuilder();

    public PrintVisitor(OutputStream outputStream) {
        this.writer = new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    @Override
    public void visit(NumberToken token) {
        result.append(token);
        result.append(" ");
        try {
            writer.write(token + " ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(Brace token) {
        result.append(token);
        result.append(" ");
        try {
            writer.write(token + " ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(Operation token) {
        result.append(token);
        result.append(" ");
        try {
            writer.write(token + " ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String result() {
        return result.toString();
    }
}
