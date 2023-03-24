package ru.ilin.manageradmin;

import ru.ilin.model.TicketEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

public interface ManagerAdmin {
    String newTicket(Instant event, long duration);

    boolean updateTicket(String id, long duration, Instant event);

    TicketEvent.UpdateTicketEvent getTicketById(String id);
}
