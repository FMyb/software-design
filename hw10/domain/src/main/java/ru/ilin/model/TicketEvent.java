package ru.ilin.model;

import java.time.Instant;

public abstract class TicketEvent {
    private String ticketId;
    private Instant time;


    public TicketEvent(String ticketId, Instant time) {
        this.ticketId = ticketId;
        this.time = time;
    }

    public TicketEvent() {

    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getTicketId() {
        return ticketId;
    }

    public Instant getTime() {
        return time;
    }

    public static class UpdateTicketEvent extends TicketEvent {
        private long duration;

        public UpdateTicketEvent(String id, Instant time, long duration) {
            super(id, time);
            this.duration = duration;
        }

        public UpdateTicketEvent() {
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public long getDuration() {
            return duration;
        }
    }

    public static class UseTicketEvent extends TicketEvent {
        private boolean isEnter;

        public UseTicketEvent(String id, Instant time, boolean isEnter) {
            super(id, time);
            this.isEnter = isEnter;
        }

        public UseTicketEvent() {
        }

        public void setEnter(boolean enter) {
            isEnter = enter;
        }

        public boolean isEnter() {
            return isEnter;
        }
    }
}
