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

public class Event implements Comparable { // пропустил при первом ревью - интерфейс Comparable параметризуется, не пренебрегай
    // Не очень понятно, чем обусловлен выбор типа для хранения времени. Обосновать можешь?
    // Если есть геттеры - непонятно, зачем public
    private LocalDateTime start;
    private LocalDateTime end;

    public Event() {}

    // Возможно ли существование событий без начала и конца?
    // Возможно ли существование событий с началом до конца?
    // Ни в коде, ни в тестах ответа нет.
    // Все равно плохо.
    //  - есть сеттеры, на них нет никакой валидации
    //  - имена переменных лживы, если уж ты решил менять их местами - то лучше дать им названия типа bound0, bound1
    public Event(LocalDateTime eventStart, LocalDateTime eventEnd) {
        // не люблю такое форматирование тренарного оператора, лучше так:
//        LocalDateTime tempStart = eventStart == null
//                ? eventStart = LocalDateTime.MIN
//                : eventStart;
        // отформатировал, и сразу стало видно, что у тебя там еще есть какое-то присваивание
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

        // я бы так:
//        if (eventStart == null) {
//            eventStart = LocalDateTime.MIN;
//        }
//        if (eventEnd == null) {
//            eventStart = LocalDateTime.MAX;
//        }
//        if (eventStart.isBefore(eventEnd)) {
//            this.start = eventStart;
//            this.end = eventEnd;
//        } else {
//            this.start = eventEnd;
//            this.end = eventStart;
//        }

        //неуместны жонглирование датами и присвоение дефолтных значений
        //валидации было бы достаточно
        //я тут вижу "слишком умный" компонент
    }

    // Обычно такие штуки делают через статический метод.
    // Непонятно, зачем тебе еще и такой конструктор. Выглядит как ненужное усложнение.
    // Можно создатьо события только для текущего дня.
    //АГ: это вариант конструктора, когда не нужды задавать дату, отличную от текущей
    // Текущая дата не является константой
    // Чо если между вызовами LocalDate.now() дата сменится?
    // Ну и в целом не вижу необходимости в таком методе он ни в условиях задачи не фигурирует, ни для тестов особо не нужен
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

    //обрати внимание - метод не используется
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setStart(LocalTime start) {
        this.start = LocalDateTime.of(LocalDate.now(), start);
    }

    //и этот тож не используется
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
    public String toString() { // чо если start и end имеют разную дату?
        return "Event{" + start.toLocalDate() +
                ": start=" + start.toLocalTime() +
                ", end=" + end.toLocalTime() +
                "}";
    }
}
