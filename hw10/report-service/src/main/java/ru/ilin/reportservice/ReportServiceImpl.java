package ru.ilin.reportservice;

import ru.ilin.model.TicketEvent;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportServiceImpl implements ReportService {
    private Map<LocalDate, List<TicketEvent.UseTicketEvent>> ticketEventMap;

    public ReportServiceImpl() {
        this.ticketEventMap = new HashMap<>();
    }

    @Override
    public void insertOne(TicketEvent.UseTicketEvent event) {
        ZoneId zoneId = ZoneId.systemDefault();
        ticketEventMap.compute(
            LocalDate.ofInstant(event.getTime(), zoneId),
            (__, list) -> {
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(event);
                return list;
            }
        );
    }

    @Override
    public Map<LocalDate, Long> getStatistics() {
        return ticketEventMap.entrySet().stream()
            .map(it -> Map.entry(
                    it.getKey(),
                    it.getValue().stream().filter(TicketEvent.UseTicketEvent::isEnter).count()
                )
            )
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x));
    }

    @Override
    public double avgCount() {
        var map = ticketEventMap.values().stream()
            .flatMap(Collection::stream)
            .filter(TicketEvent.UseTicketEvent::isEnter)
            .collect(Collectors.toMap(
                TicketEvent::getTicketId,
                it -> 1.0,
                Double::sum
            ));
        return map.values().stream().mapToDouble(it -> it).sum() / map.size();
    }

    @Override
    public double avgDuration() {
        var events = ticketEventMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        double duration = 0;
        double count = 0;
        for (var event : events) {
            if (event.isEnter()) {
                var eventEnd = events.stream()
                    .filter(it -> it.getTicketId().equals(event.getTicketId()) && it.getTime().isAfter(event.getTime()))
                    .min(Comparator.comparing(TicketEvent::getTime))
                    .orElse(event);
                if (!eventEnd.equals(event)) {
                    duration += eventEnd.getTime().getEpochSecond() - event.getTime().getEpochSecond();
                    count++;
                }
            }
        }
        return duration / count;
    }
}
