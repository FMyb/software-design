package ru.ilin.eventstore;

import ru.ilin.model.TicketEvent;

import java.time.Instant;

public interface EventStore {
    String newTicket(Instant event, long duration);

    boolean updateTicket(String id, long duration, Instant event);

    boolean enter(String id, boolean isEnter, Instant event);

    TicketEvent.UpdateTicketEvent getTicketById(String id);
}
