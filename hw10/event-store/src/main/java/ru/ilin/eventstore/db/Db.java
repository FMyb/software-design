package ru.ilin.eventstore.db;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import ru.ilin.model.TicketEvent;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Db {
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<TicketEvent.UpdateTicketEvent> updateTicketEvents;
    private final MongoCollection<TicketEvent.UseTicketEvent> useTicketEvents;


    public Db(int port) {
        CodecRegistry pojoCodecRegistry = fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
        mongoClient = MongoClients.create("mongodb://localhost:" + port);
        database = mongoClient.getDatabase("eventSource").withCodecRegistry(pojoCodecRegistry);
        updateTicketEvents = database.getCollection("updateTicket", TicketEvent.UpdateTicketEvent.class);
        useTicketEvents = database.getCollection("useTicket", TicketEvent.UseTicketEvent.class);
        updateTicketEvents.createIndex(Indexes.hashed("ticket_id"));
        updateTicketEvents.createIndex(Indexes.ascending("time"));
        useTicketEvents.createIndex(Indexes.hashed("ticket_id"));
        useTicketEvents.createIndex(Indexes.ascending("time"));
    }

    public TicketEvent.UpdateTicketEvent getTicketById(String id) {
        return updateTicketEvents.find(Filters.eq("ticketId", id))
            .sort(Sorts.descending("time"))
            .limit(1)
            .first();
    }

    public boolean updateSubscription(TicketEvent.UpdateTicketEvent updateTicketEvent) {
        return updateTicketEvents.insertOne(updateTicketEvent).wasAcknowledged();
    }

    public boolean useTicket(TicketEvent.UseTicketEvent useTicketEvent) {
        TicketEvent.UpdateTicketEvent ticket = getTicketById(useTicketEvent.getTicketId());
        TicketEvent.UseTicketEvent lastUseTicket = useTicketEvents.find(Filters.eq(
                "ticketId",
                useTicketEvent.getTicketId()
            ))
            .sort(Sorts.descending("time"))
            .limit(1)
            .first();
        ZoneId zoneId = ZoneId.systemDefault();
        if ((lastUseTicket != null && lastUseTicket.isEnter() == useTicketEvent.isEnter()) || ticket.getTime()
            .atZone(zoneId)
            .plus(Duration.of(ticket.getDuration(), ChronoUnit.MILLIS))
            .isBefore(Instant.now().atZone(zoneId))) {
            return false;
        }
        return useTicketEvents.insertOne(useTicketEvent).wasAcknowledged();
    }

    public List<TicketEvent.UseTicketEvent> getAllUses() {
        List<TicketEvent.UseTicketEvent> ticketEvents = new ArrayList<>();
        useTicketEvents.find().forEach(ticketEvents::add);
        return ticketEvents;
    }

}
