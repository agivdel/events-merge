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
    // Не очень понятно, чем обусловлен выбор типа для хранения времени. Обосновать можешь?
    // Если есть геттеры - непонятно, зачем public
    public LocalDateTime start;
    public LocalDateTime end;

    public Event() { // Используется только в тестах - не нужен.
    }

    // Возможно ли существование событий без начала и конца?
    // Возможно ли существование событий с началом до конца?
    // Ни в коде, ни в тестах ответа нет.
    public Event(LocalDateTime eventStart, LocalDateTime eventEnd) {
        this.start = eventStart;
        this.end = eventEnd;
    }

    // Обычно такие штуки делают через статический метод.
    // Непонятно, зачем тебе еще и такой конструктор. Выглядит как ненужное усложнение.
    // Можно создатьо события только для текущего дня.
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
