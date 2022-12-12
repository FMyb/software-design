package ru.yandex.ilin;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.ilin.tokenizer.impl.TokenizerImpl;
import ru.yandex.ilin.tokens.Token;
import ru.yandex.ilin.tokens.impl.Brace;
import ru.yandex.ilin.tokens.impl.NumberToken;
import ru.yandex.ilin.tokens.impl.Operation;
import ru.yandex.ilin.visitors.TokenVisitor;
import ru.yandex.ilin.visitors.impl.CalcVisitor;
import ru.yandex.ilin.visitors.impl.ParserVisitor;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Tests {
    private ParserVisitor parserVisitor;
    private CalcVisitor calcVisitor;

    @Before
    public void setUp() {
        parserVisitor = new ParserVisitor();
        calcVisitor = new CalcVisitor();
    }

    @Test
    public void testSum() {
        toTokens("1 + 1").forEach(it -> it.accept(parserVisitor));
        List<Token> parsed = parserVisitor.result();
        Assert.assertEquals(
            List.of(new NumberToken(1), new NumberToken(1), Operation.sumOperation()),
            parsed
        );
        parsed.forEach(it -> it.accept(calcVisitor));
        Assert.assertEquals(1 + 1, calcVisitor.result().intValue());
    }

    @Test
    public void testSub() {
        toTokens("1 - 1").forEach(it -> it.accept(parserVisitor));
        List<Token> parsed = parserVisitor.result();
        Assert.assertEquals(
            List.of(new NumberToken(1), new NumberToken(1), Operation.subOperation()),
            parsed
        );
        parsed.forEach(it -> it.accept(calcVisitor));
        Assert.assertEquals(1 - 1, calcVisitor.result().intValue());
    }

    @Test
    public void testMul() {
        toTokens("1 * 1").forEach(it -> it.accept(parserVisitor));
        List<Token> parsed = parserVisitor.result();
        Assert.assertEquals(
            List.of(new NumberToken(1), new NumberToken(1), Operation.mulOperation()),
            parsed
        );
        parsed.forEach(it -> it.accept(calcVisitor));
        Assert.assertEquals(1 * 1, calcVisitor.result().intValue());
    }

    @Test
    public void testDiv() {
        toTokens("1 / 1").forEach(it -> it.accept(parserVisitor));
        List<Token> parsed = parserVisitor.result();
        Assert.assertEquals(
            List.of(new NumberToken(1), new NumberToken(1), Operation.divOperation()),
            parsed
        );
        parsed.forEach(it -> it.accept(calcVisitor));
        Assert.assertEquals(1 / 1, calcVisitor.result().intValue());
    }

    @Test
    public void testSimplExpression() {
        toTokens("(1 + 1) * (1 - 1) + 6 / 2").forEach(it -> it.accept(parserVisitor));
        List<Token> parsed = parserVisitor.result();
        Assert.assertEquals(
            List.of(
                new NumberToken(1),
                new NumberToken(1),
                Operation.sumOperation(),
                new NumberToken(1),
                new NumberToken(1),
                Operation.subOperation(),
                Operation.mulOperation(),
                new NumberToken(6),
                new NumberToken(2),
                Operation.divOperation(),
                Operation.sumOperation()
            ),
            parsed
        );
        parsed.forEach(it -> it.accept(calcVisitor));
        Assert.assertEquals((1 + 1) * (1 - 1) + 6 / 2, calcVisitor.result().intValue());
    }

    private List<Token> toTokens(String input) {
        return new TokenizerImpl(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))).all();
    }
}
