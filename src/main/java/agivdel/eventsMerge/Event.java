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
    private LocalDateTime start;
    private LocalDateTime end;

    public Event() {}

    // Возможно ли существование событий без начала и конца?
    // Возможно ли существование событий с началом до конца?
    // Ни в коде, ни в тестах ответа нет.
    // Все равно плохо.
    //  - есть сеттеры, на них нет никакой валидации (АГ: сделал валдиацию сеттеров DB: не сделал, через сеттеры по прежнему можно проставить начало позже конца)
    //  - имена переменных лживы, если уж ты решил менять их местами - то лучше дать им названия типа bound0, bound1
    public Event(LocalDateTime eventStart, LocalDateTime eventEnd) {
        eventStart = minInsteadNull(eventStart);
        eventEnd = maxInsteadNull(eventEnd);
        if (eventStart.isBefore(eventEnd)) {
            this.start = eventStart;
            this.end = eventEnd;
        } else {
            this.start = eventEnd;
            this.end = eventStart;
        }
        //неуместны жонглирование датами (АГ: в каких строках? DB: странный вопрос, тут не было ничего кроме жонглирования датами и присвоения дефолтных значений) и присвоение дефолтных значений (АГ: ты про MIN/MAX? ДБ - да)
        //валидации было бы достаточно
        //я все еще тут вижу "слишком умный" компонент
    }

    // Обычно такие штуки делают через статический метод.
    // АГ: ты имееешь в виду методы типа LocalTime.of() или что-то другое?
    // Непонятно, зачем тебе еще и такой конструктор. Выглядит как ненужное усложнение.
    // Можно создатьо события только для текущего дня.
    //АГ: это вариант конструктора, когда не нужды задавать дату, отличную от текущей
    // Текущая дата не является константой
    // Чо если между вызовами LocalDate.now() дата сменится?
    // Ну и в целом не вижу необходимости в таком методе он ни в условиях задачи не фигурирует, ни для тестов особо не нужен
    //АГ: Если дата не нужна, ввести только время проще, чем время и дату. Думаю, иначе легко скатится к API Book
    //Не понял наброса. Как отсутствие необязательного констуктора приближает нас к API Book?
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
    //АГ: но он может использоваться - если пользователю будет удобнее оперировать LocalDateTime (например, он уже получит такой объект готовым).
    //АГ: а только для тестов мне было лень набивать LocalDateTime вместо LocalTime.
    //Что мешало хелпер для тестов написать?
    public void setStart(LocalDateTime start) {
        this.start = minInsteadNull(start);
    }

    public void setStart(LocalTime start) {
        this.start = LocalDateTime.of(LocalDate.now(), start == null ? LocalTime.MIDNIGHT : start);
    }

    //и этот тож не используется
    public void setEnd(LocalDateTime end) {
        this.end = maxInsteadNull(end);
    }

    public void setEnd(LocalTime end) {
        this.end = LocalDateTime.of(LocalDate.now(), end == null ? LocalTime.MAX : end);
    }

    //Валидация конструкторов и сеттеров
    //Это не валидация а присвоение дефолтного значения
    private LocalDateTime minInsteadNull(LocalDateTime start) {
        if (start == null) {
            return LocalDateTime.MIN;
        }
        return start;
    }

    //Валидация конструкторов и сеттеров
    //Это не валидация а присвоение дефолтного значения
    private LocalDateTime maxInsteadNull(LocalDateTime end) {
        if (end == null) {
            return LocalDateTime.MAX;
        }
        return end;
    }

    @Override
    public int compareTo(Object o) { // Так ли нужно сравнение именно здесь? Подумай, обсудим.
        Event e = (Event) o;
        return this.getStart().compareTo(e.getStart());
        //АГ: вот это не понимаю - если я хочу, чтобы Event были сравниваемые, где мне еще переопределять этот метод?
        //Или ты имеешь в виду писать компаратор в класса Merge (где он требуется для сортировки)?
        //Компаратор можно определить и здесь. Но именно компаратор - как константу с говорящим именем.
        //Потому что у класса Event нет самоочевидного способа сортировки. Сортировка по датам конца ничуть не хуже чем по датам начала.
        //Вроятно, нужен тест, в котором на вход мержера будет подаваться множество со своим компаратором, который отличается
    }

    @Override
    public String toString() { // чо если start и end имеют разную дату? обламываешься починить?
        return "Event{" + start.toLocalDate() +
                ": start=" + start.toLocalTime() +
                ", end=" + end.toLocalTime() +
                "}";
    }
}
