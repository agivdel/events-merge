package agivdel.eventsMerge;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Класс Event с двумя полями LocalDateTime:
 * start: время начала события.
 * end: время конца события.
 * Если через конструктор передать LocalDateTime=null, start равен LocalDateTime.MIN.
 * Если через конструктор передать LocalTime=null, start равен LocalTime.MIDNIGHT.
 * Если через конструктор передать LocalDateTime=null, end равен LocalDateTime.MAX.
 * Если через конструктор передать LocalTime=null, end равен LocalTime.MAX.
 * При передаче через сеттер только объекта LocalTime устанавливается текущая дата.
 * При сравнении двух объектов Event сравниваются поля start.
 */

public class Event implements Comparable {
    // Не очень понятно, чем обусловлен выбор типа для хранения времени. Обосновать можешь?
    // Если есть геттеры - непонятно, зачем public
    private LocalDateTime start;
    private LocalDateTime end;

    public Event() {}

    // Возможно ли существование событий без начала и конца?
    // Возможно ли существование событий с началом до конца?
    // Ни в коде, ни в тестах ответа нет.
    public Event(LocalDateTime eventStart, LocalDateTime eventEnd) {
        LocalDateTime tempStart = eventStart == null ? eventStart = LocalDateTime.MIN : eventStart;
        LocalDateTime tempEnd = eventEnd == null ? eventEnd = LocalDateTime.MAX : eventEnd;
        LocalDateTime temp;
        if (eventEnd.isBefore(eventStart)) {
            temp = tempEnd;
            tempEnd = tempStart;
            tempStart = temp;
        }
        this.start = tempStart;
        this.end = tempEnd;
    }

    // Обычно такие штуки делают через статический метод.
    // Непонятно, зачем тебе еще и такой конструктор. Выглядит как ненужное усложнение.
    // Можно создатьо события только для текущего дня.
    //АГ: это вариант конструктора, когда не нужды задавать дату, отличную от текущей
    public Event(LocalTime eventStart, LocalTime eventEnd) {
        this(LocalDateTime.of(LocalDate.now(), eventStart == null ? LocalTime.MIDNIGHT : eventStart),
                LocalDateTime.of(LocalDate.now(), eventEnd == null ? LocalTime.MAX : eventEnd));
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

    public void setStart(LocalTime start) {
        this.start = LocalDateTime.of(LocalDate.now(), start);
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setEnd(LocalTime end) {
        this.end = LocalDateTime.of(LocalDate.now(), end);
    }

    @Override
    public int compareTo(Object o) { // Так ли нужно сравнение именно здесь? Подумай, обсудим.
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
