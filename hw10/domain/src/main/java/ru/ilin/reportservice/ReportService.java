package ru.ilin.reportservice;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import ru.ilin.model.TicketEvent;

import java.time.LocalDate;
import java.util.Map;

public interface ReportService {

    void insertOne(TicketEvent.UseTicketEvent event);

    Map<LocalDate, Long> getStatistics();

    double avgCount();

    double avgDuration();
}
