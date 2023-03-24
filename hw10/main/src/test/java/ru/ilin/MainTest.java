package ru.ilin;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;
import ru.ilin.eventstore.EventStore;
import ru.ilin.eventstore.EventStoreImpl;
import ru.ilin.eventstore.db.Db;
import ru.ilin.manageradmin.ManagerAdmin;
import ru.ilin.manageradmin.ManagerAdminImpl;
import ru.ilin.model.TicketEvent;
import ru.ilin.reportservice.ReportService;
import ru.ilin.reportservice.ReportServiceImpl;
import ru.ilin.turnstile.Turnstile;
import ru.ilin.turnstile.TurnstileImpl;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainTest {
    final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    ReportService reportService;
    EventStore eventStore;
    ManagerAdmin managerAdmin;
    Turnstile turnstile;

    @Before
    public void setUp() throws Exception {
        mongoDBContainer.start();
        reportService = new ReportServiceImpl();
        eventStore = new EventStoreImpl(new Db(mongoDBContainer.getFirstMappedPort()), reportService);
        managerAdmin = new ManagerAdminImpl(eventStore);
        turnstile = new TurnstileImpl(eventStore);
    }

    @After
    public void tearDown() {
        mongoDBContainer.stop();
    }

    @Test
    public void testManagerAdmin() {
        Instant time = Instant.now().atZone(ZoneId.systemDefault()).toInstant();
        String ticketId = managerAdmin.newTicket(time, TimeUnit.DAYS.toMillis(1));
        TicketEvent.UpdateTicketEvent ticket = managerAdmin.getTicketById(ticketId);
        Assert.assertEquals(ticketId, ticket.getTicketId());
        Assert.assertEquals(TimeUnit.DAYS.toMillis(1), ticket.getDuration());
        Assert.assertEquals(time.getEpochSecond(), ticket.getTime().getEpochSecond());
        boolean f = managerAdmin.updateTicket(ticketId, TimeUnit.DAYS.toMillis(2), time.plus(Duration.ofDays(2)));
        Assert.assertTrue(f);
        ticket = managerAdmin.getTicketById(ticketId);
        Assert.assertEquals(ticketId, ticket.getTicketId());
        Assert.assertEquals(TimeUnit.DAYS.toMillis(2), ticket.getDuration());
        Assert.assertEquals(time.plus(Duration.ofDays(2)).getEpochSecond(), ticket.getTime().getEpochSecond());
    }

    @Test
    public void testTurnstile() {
        Instant time = Instant.now().atZone(ZoneId.systemDefault()).toInstant();
        String ticketId = managerAdmin.newTicket(time, TimeUnit.DAYS.toMillis(1));
        Assert.assertTrue(turnstile.enter(time, ticketId));
        Assert.assertFalse(turnstile.enter(time.plus(Duration.ofSeconds(30)), ticketId));
        Assert.assertTrue(turnstile.exit(time.plus(Duration.ofSeconds(60)), ticketId));
        Assert.assertFalse(turnstile.exit(time.plus(Duration.ofSeconds(90)), ticketId));
    }

    @Test
    public void testReport() {
        Instant time = Instant.now().atZone(ZoneId.systemDefault()).toInstant();
        String ticketId = managerAdmin.newTicket(time, TimeUnit.DAYS.toMillis(1));
        Assert.assertTrue(turnstile.enter(time, ticketId));
        Assert.assertFalse(turnstile.enter(time.plus(Duration.ofSeconds(30)), ticketId));
        Assert.assertTrue(turnstile.exit(time.plus(Duration.ofSeconds(60)), ticketId));
        Assert.assertFalse(turnstile.exit(time.plus(Duration.ofSeconds(90)), ticketId));
        Assert.assertEquals(Map.of(LocalDate.now(ZoneId.systemDefault()), 1L),reportService.getStatistics());
        Assert.assertEquals(60.0, reportService.avgDuration(), 0.001);
        Assert.assertEquals(1.0, reportService.avgCount(), 0.001);
    }
}
