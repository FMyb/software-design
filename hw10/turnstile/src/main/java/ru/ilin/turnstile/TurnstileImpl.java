package ru.ilin.turnstile;

import ru.ilin.eventstore.EventStore;

import java.time.Instant;

public class TurnstileImpl implements Turnstile {
    private final EventStore eventStore;

    public TurnstileImpl(EventStore eventStore) {this.eventStore = eventStore;}

    @Override
    public boolean enter(Instant event, String id) {
        return eventStore.enter(id, true, event);
    }

    @Override
    public boolean exit(Instant event, String id) {
        return eventStore.enter(id, false, event);
    }
}
