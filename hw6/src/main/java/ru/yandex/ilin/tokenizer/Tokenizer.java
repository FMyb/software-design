package ru.yandex.ilin.tokenizer;

import ru.yandex.ilin.tokens.Token;

import java.util.List;
import java.util.Optional;

public interface Tokenizer {
    Optional<Token> next();

    List<Token> all();
}
