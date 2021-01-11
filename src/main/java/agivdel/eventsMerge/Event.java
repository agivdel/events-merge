package agivdel.eventsMerge;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Класс Event с двумя полями LocalDateTime:
 * start: время начала события.
 * end: время конца события.
 * Аргументы конструктра и метода today() не могут быть null.
 * Первым аргументом констуктора и метода today() должно быть время начала события.
 * При сравнении двух объектов Event сравниваются поля start.
 */
public class Event implements Comparable<Event> {
    private final LocalDateTime start;
    private final LocalDateTime end;


    public Event(LocalDateTime start, LocalDateTime end) {
        checkIsNotNull(start, "The event start can`t be null.");
        checkIsNotNull(end, "The event end can`t be null.");
        checkNaturalOrder(start, end);
        this.start = start;
        this.end = end;
    }

    public static Event today(LocalTime start, LocalTime end) {
        checkIsNotNull(start, "The event start can`t be null.");
        checkIsNotNull(end, "The event end can`t be null.");
        LocalDate today = LocalDate.now();
        return new Event(LocalDateTime.of(today, start),
                LocalDateTime.of(today, end));
    }

    public LocalDateTime getStart() {
        return this.start;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }

    private static void checkIsNotNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void checkNaturalOrder(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("The event start can't be later than the event end.");
        }
    }

    @Override
    public int compareTo(Event e) {
        return this.getStart().compareTo(e.getStart());
    }

    @Override
    public String toString() {
        return "Event: " + start.toLocalDate() + ", " + start.toLocalTime()
                + " - " + end.toLocalDate() + ", " + end.toLocalTime() + ".";
    }
}
