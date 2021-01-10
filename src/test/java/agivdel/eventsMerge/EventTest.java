package agivdel.eventsMerge;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//Я не очень часто пишу тесты на POJO
//Но по хорошему они нужны
//Существуют библиотеки, которые умеюи тестить equals, hashCode, геттеры и сеттеры автоматом.
//Большая часть этих тестов (все кроме последнего) обусловлена подходом, который ты выбрал при реализации Event
//Их могло бы быть меньше, и они были бы проще
public class EventTest {
    private final LocalDate now = LocalDate.now();

    private final LocalTime t1 = LocalTime.of(10, 0);
    private final LocalTime t2 = LocalTime.of(10, 30);
    private final LocalTime t3 = LocalTime.of(11, 0);
    private final LocalTime t4 = LocalTime.of(11, 30);
    private final LocalTime t5 = LocalTime.of(12, 0);
    private final LocalTime t6 = LocalTime.of(12, 30);
    private final LocalTime t7 = LocalTime.of(13, 0);
    private final LocalTime t8 = LocalTime.of(13, 30);
    private final LocalTime t9 = LocalTime.of(14, 0);


    @Test
    public void eventFromLocalTimeTest() {
        Event event = new Event(t1, t2);
        Assert.assertEquals(10, event.getStart().getHour());
        Assert.assertEquals(0, event.getStart().getMinute());
        Assert.assertEquals(LocalTime.of(10, 30), event.getEnd().toLocalTime());
        Assert.assertEquals(LocalDate.now(), event.getStart().toLocalDate()); // нестабильнеый тест
    }

    @Test
    public void eventFromBeginningOfTimeTest() {
        Event nullStart = new Event(null, LocalDateTime.of(now, t3));
        Assert.assertEquals(LocalDateTime.MIN, nullStart.getStart());
    }

    @Test
    public void eventToEndOfTimeTest() {
        Event nullEnd = new Event(LocalDateTime.of(now, t4), null);
        Assert.assertEquals(LocalDateTime.MAX, nullEnd.getEnd());
    }

    @Test
    public void eventFromMidnightTest() {
        Event fromMidnight = new Event(null, t5);
        Assert.assertEquals(LocalTime.MIDNIGHT, fromMidnight.getStart().toLocalTime());
    }

    @Test
    public void eventToAlmostMidnightTest() {
        Event toMidnight = new Event(t9, null);
        Assert.assertEquals(LocalTime.MAX, toMidnight.getEnd().toLocalTime());
    }

    @Test
    public void eventComparingTest() {
        Event event0 = new Event();
        event0.setStart(LocalDateTime.of(now, t5));
        event0.setEnd(LocalDateTime.of(now, t7));

        Event event1 = new Event();
        event1.setStart(t5);
        event1.setEnd(t7);
        Assert.assertEquals(0, event0.compareTo(event1));

        Event event2 = new Event(t6, t8);
        Assert.assertEquals(-1, event0.compareTo(event2));

        Event event3 = new Event(t4, t9);
        Assert.assertEquals(1, event0.compareTo(event3));
    }

    //Хороший тест
    @Test
    public void toStringTest() {
        String result = "Event{" + now + ": start=12:00, end=14:00}";
        Event event = new Event(t5, t9);
        Assert.assertEquals(result, event.toString());
    }
}
