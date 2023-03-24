package ru.ilin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.ilin.eventstore.EventStore;
import ru.ilin.eventstore.EventStoreImpl;
import ru.ilin.eventstore.db.Db;
import ru.ilin.manageradmin.ManagerAdmin;
import ru.ilin.manageradmin.ManagerAdminImpl;
import ru.ilin.reportservice.ReportService;
import ru.ilin.reportservice.ReportServiceImpl;
import ru.ilin.turnstile.Turnstile;
import ru.ilin.turnstile.TurnstileImpl;

import java.time.Instant;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        ReportService reportService = new ReportServiceImpl();
        EventStore eventStore = new EventStoreImpl(new Db(27017), reportService);
        Turnstile turnstile = new TurnstileImpl(eventStore);
        ManagerAdmin managerAdmin = new ManagerAdminImpl(eventStore);
        String str = "";
        Scanner scanner = new Scanner(System.in);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        while (!Objects.equals(str, "exit") && scanner.hasNextLine()) {
            str = scanner.nextLine();
            var command = str.split("\s");
            System.out.println(objectMapper.writeValueAsString(switch (command[0]) {
                case "m" -> switch (command[1]) {
                    case "add" -> managerAdmin.newTicket(Instant.now(), TimeUnit.DAYS.toMillis(1));
                    case "upd" -> managerAdmin.updateTicket(command[2], TimeUnit.DAYS.toMillis(2), Instant.now());
                    case "get" -> managerAdmin.getTicketById(command[2]);
                    default -> "unknown command: m " + command[1];
                };
                case "t" -> switch (command[1]) {
                    case "enter" -> turnstile.enter(Instant.now(), command[2]);
                    case "exit" -> turnstile.exit(Instant.now(), command[2]);
                    default -> "unknown command: t " + command[1];
                };
                case "r" -> switch (command[1]) {
                    case "stat" -> reportService.getStatistics();
                    case "cnt" -> reportService.avgCount();
                    case "dur" -> reportService.avgDuration();
                    default -> "unknown command: r " + command[1];
                };
                case "exit" -> "exit";
                default -> "unknown type: " + command[0];
            }));
        }
    }
}
