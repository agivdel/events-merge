package agivdel.eventsMerge;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class EventTest {
    private final LocalDate today = LocalDate.now();

    private final LocalTime t1 = LocalTime.of(10, 0);
    private final LocalTime t2 = LocalTime.of(10, 30);
    private final LocalTime t3 = LocalTime.of(11, 0);
    private final LocalTime t4 = LocalTime.of(11, 30);
    private final LocalTime t5 = LocalTime.of(12, 0);
    private final LocalTime t6 = LocalTime.of(12, 30);
    private final LocalTime t7 = LocalTime.of(13, 0);
    private final LocalTime t8 = LocalTime.of(13, 30);
    private final LocalTime t9 = LocalTime.of(14, 0);

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void event_ConstructorWithNullStart() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The event start can`t be null.");
        Event e = new Event(null, LocalDateTime.of(today, t2));
    }

    @Test
    public void event_ConstructorWithNullEnd() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The event end can`t be null.");
        Event e = new Event(LocalDateTime.of(today, t2), null);
    }

    @Test
    public void event_TodayWithNullStart() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The event start can`t be null.");
        Event e = Event.today(null, t7);
    }

    @Test
    public void event_TodayWithNullEnd() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The event end can`t be null.");
        Event e = Event.today(t3, null);
    }

    @Test
    public void event_StartLaterThenEndTest() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The event start can't be later than the event end.");
        Event e = Event.today(t5, t1);
    }

    @Test
    public void event_Comparing() {
        Event event0 = new Event(LocalDateTime.of(today, t4), LocalDateTime.of(today, t6));

        Event event1 = Event.today(t4, t6);
        Assert.assertEquals(0, event0.compareTo(event1));

        Event event2 = Event.today(t5, t7);
        Assert.assertEquals(-1, event0.compareTo(event2));

        Event event3 = new Event(LocalDateTime.of(today, t2), LocalDateTime.of(today, t3));
        Assert.assertEquals(1, event0.compareTo(event3));
    }

    @Test
    public void event_ToString() {
        Event event = new Event(LocalDateTime.of(today, t5), LocalDateTime.of(today, t9));
        String result = "Event: " + event.getStart().toLocalDate() + ", 12:00 - " + event.getEnd().toLocalDate() + ", 14:00.";
        Assert.assertEquals(result, event.toString());
    }
}
