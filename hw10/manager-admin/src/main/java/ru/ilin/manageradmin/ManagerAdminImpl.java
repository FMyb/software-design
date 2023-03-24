package ru.ilin.manageradmin;

import ru.ilin.eventstore.EventStore;
import ru.ilin.model.TicketEvent;

import java.time.Instant;

public class ManagerAdminImpl implements ManagerAdmin {
    private final EventStore eventStore;

    public ManagerAdminImpl(EventStore eventStore) {this.eventStore = eventStore;}

    @Override
    public String newTicket(Instant event, long duration) {
        return eventStore.newTicket(event, duration);
    }

    @Override
    public boolean updateTicket(String id, long duration, Instant event) {
        return eventStore.updateTicket(id, duration, event);
    }

    @Override
    public TicketEvent.UpdateTicketEvent getTicketById(String id) {
        return eventStore.getTicketById(id);
    }
}
