package agivdel.eventsMerge;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Класс Event с двумя полями LocalDateTime:
 * start: время начала события,
 * end: время конца события.
 * При сравнении двух объектов Event сравниваются поля start.
 */

public class Event implements Comparable {
    public LocalDateTime start;
    public LocalDateTime end;

    public Event() {
    }

    public Event(LocalDateTime eventStart, LocalDateTime eventEnd) {
        this.start = eventStart;
        this.end = eventEnd;
    }

    public Event(LocalTime eventStart, LocalTime eventEnd) {
        this(LocalDateTime.of(LocalDate.now(), eventStart), LocalDateTime.of(LocalDate.now(), eventEnd));
    }

    public LocalDateTime getStart() {
        return this.start;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    @Override
    public int compareTo(Object o) {
        Event e = (Event) o;
        return this.getStart().compareTo(e.getStart());
    }

    @Override
    public String toString() {
        return "Event{" + start.toLocalDate() +
                ": start=" + start.toLocalTime() +
                ", end=" + end.toLocalTime() +
                "}";
    }
}
