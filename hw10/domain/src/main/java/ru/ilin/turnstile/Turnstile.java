package ru.ilin.turnstile;

import java.time.Instant;

public interface Turnstile {
    boolean enter(Instant event, String id);

    boolean exit(Instant event, String id);
}
