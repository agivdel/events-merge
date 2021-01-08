package agivdel.eventsMerge;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//Я не очень часто пишу тесты на POJO
//Но по хорошему они нужны
//Существуют библиотеки, которые умеюи тестить equals, hashCode, геттеры и сеттеры автоматом.
public class EventTest {

    // Если ты создаешь event в каждом методе - никакого смысла нет делать его полем.
    private Event event;

    // Очень странно навзание поля
    // Я бы назвал now или today
    private final LocalDate ld = LocalDate.now();

    // Я так понимаю, ты хотел иметь набор дат, которые точно больше одна другой
    // Я бы назвал их d0, d1, ..., dn
    private final LocalTime lt1000 = LocalTime.of(10, 0);
    private final LocalTime lt1030 = LocalTime.of(10, 30);
    private final LocalTime lt1100 = LocalTime.of(11, 0);
    private final LocalTime lt1130 = LocalTime.of(11, 30);
    private final LocalTime lt1200 = LocalTime.of(12, 0);
    private final LocalTime lt1230 = LocalTime.of(12, 30);
    private final LocalTime lt1300 = LocalTime.of(13, 0);
    private final LocalTime lt1330 = LocalTime.of(13, 30);
    private final LocalTime lt1400 = LocalTime.of(14, 0);


    @Test
    public void eventFromDefaultConstructorTest() { // Что ты тут тестируешь?
        event = new Event();
        Assert.assertNotNull(event);
    }

    @Test
    public void eventFromLocalTimeTest() {
        event = new Event(lt1000, lt1030);
        Assert.assertEquals(10, event.getStart().getHour());
        Assert.assertEquals(0, event.getStart().getMinute());
        Assert.assertEquals(LocalTime.of(10, 30), event.getEnd().toLocalTime());
        Assert.assertEquals(LocalDate.now(), event.getStart().toLocalDate()); // нестабильнеый тест
    }

    //Ты тут тестируешь что? Ты тут тестируешь LocalDateTime, LocalTime и LocalDate.
    @Test
    public void eventFromLocalDateTimeTest() {
        event = new Event(LocalDateTime.of(ld, lt1100), LocalDateTime.of(ld, lt1130));
        Assert.assertEquals(11, event.getEnd().getHour());
        Assert.assertEquals(30, event.getEnd().getMinute());
        Assert.assertEquals(LocalTime.of(11, 0), event.getStart().toLocalTime());
        Assert.assertEquals(ld, event.getStart().toLocalDate());
    }

    @Test
    public void eventComparingTest() {
        event = new Event();
        event.setStart(LocalDateTime.of(ld, lt1200));
        event.setEnd(LocalDateTime.of(ld, lt1230));

        Event event2 = new Event(lt1200, lt1300);
        Assert.assertEquals(0, event.compareTo(event2));

        //Не нужно переиспользовать переменные без особой на то нужды.
        //Периспользование переменных при копипасте - это путь к странным ошибками.
        //Не копипастить ты не можешь, уж поверь мне.
        //А не переиспользовать переменные - можешь.
        event2 = new Event(lt1230, lt1330);
        Assert.assertEquals(-1, event.compareTo(event2));

        event2 = new Event(lt1130, lt1400);
        Assert.assertEquals(1, event.compareTo(event2));
    }

    //Хороший тест
    @Test
    public void toStringTest() {
        String result = "Event{2021-01-08: start=12:00, end=14:00}";
        event = new Event(lt1200, lt1400);
        Assert.assertEquals(result, event.toString());
    }
}
