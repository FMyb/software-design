package ru.ilin.eventstore;

import ru.ilin.eventstore.db.Db;
import ru.ilin.model.TicketEvent;
import ru.ilin.reportservice.ReportService;

import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

public class EventStoreImpl implements EventStore {
    private final Db db;
    private final ReportService reportService;

    public EventStoreImpl(Db db, ReportService reportService) {
        this.db = db;
        this.reportService = reportService;

        db.getAllUses().forEach(reportService::insertOne);
    }


    @Override
    public String newTicket(Instant event, long duration) {
        ZoneId zoneId = ZoneId.systemDefault();
        TicketEvent.UpdateTicketEvent ticket = new TicketEvent.UpdateTicketEvent(
            UUID.randomUUID().toString(),
            event.atZone(zoneId).toInstant(),
            duration
        );
        if (db.updateSubscription(ticket)) {
            return ticket.getTicketId();
        } else {
            return null;
        }
    }

    @Override
    public boolean updateTicket(String id, long duration, Instant event) {
        ZoneId zoneId = ZoneId.systemDefault();
        TicketEvent.UpdateTicketEvent ticketEvent = new TicketEvent.UpdateTicketEvent(
            id,
            event.atZone(zoneId).toInstant(),
            duration
        );
        return db.updateSubscription(ticketEvent);
    }

    @Override
    public boolean enter(String id, boolean isEnter, Instant event) {
        ZoneId zoneId = ZoneId.systemDefault();
        TicketEvent.UseTicketEvent ticketEvent = new TicketEvent.UseTicketEvent(
            id,
            event.atZone(zoneId).toInstant(),
            isEnter
        );
        boolean f = db.useTicket(ticketEvent);
        if (f) {
            reportService.insertOne(ticketEvent);
        }
        return f;
    }

    @Override
    public TicketEvent.UpdateTicketEvent getTicketById(String id) {
        return db.getTicketById(id);
    }
}
