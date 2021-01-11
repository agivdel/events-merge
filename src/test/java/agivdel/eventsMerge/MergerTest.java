package agivdel.eventsMerge;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class MergerTest {

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
    public void setToMerge_WithNullSet() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The eventSet can't be null.");
        Merger.setToMerge(null);
    }

    @Test
    public void setToMerge_WithEmptySet() {
        Set<Event> input = new HashSet<>();
        Set<Event> result = Merger.setToMerge(input);
        Assert.assertSame(result, input);
    }

    @Test
    public void setToMerge_WithOnlyNotNullElement() {
        Set<Event> input = new LinkedHashSet<>();
        input.add(new Event(LocalDateTime.of(LocalDate.now(), t1), LocalDateTime.of(LocalDate.now(), t3)));

        Set<Event> result = Merger.setToMerge(input);
        Assert.assertSame(input, result);
    }

    @Test
    public void setToMerge_WithNullElement() {
        Event e = Event.today(t3, t6);
        Set<Event> inputWithNull = new HashSet<>();
        inputWithNull.add(null);
        inputWithNull.add(e);

        Set<Event> result = Merger.setToMerge(inputWithNull);
        Assert.assertNotSame(result, inputWithNull);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(e, result.stream().findFirst().get());
    }

    @Test
    public void setToMerge_WithoutOverlap() {
        Event e1 = new Event(LocalDateTime.of(LocalDate.now(), t1), LocalDateTime.of(LocalDate.now(), t8));
        Event e2 = Event.today(t8, t9);
        Set<Event> eventSet = Set.of(e1, e2);

        Set<Event> notMergedSet = Merger.setToMerge(eventSet);
        Assert.assertEquals(eventSet, notMergedSet);
    }

    @Test
    public void setToMerge_WithOverlap() {
        Set<Event> eventSet = new TreeSet<>();
        eventSet.add(Event.today(t1, t2));
        eventSet.add(Event.today(t2, t5));
        eventSet.add(Event.today(t4, t7));
        eventSet.add(Event.today(t7, t9));

        Set<Event> mergedSet = Merger.setToMerge(eventSet);
        List<Event> result = new ArrayList<>(mergedSet);

        Assert.assertEquals(3, result.size());

        Event expectedEvent0 = Event.today(t1, t2);
        Assert.assertEquals(expectedEvent0.getStart(), result.get(0).getStart());
        Assert.assertEquals(expectedEvent0.getEnd(), result.get(0).getEnd());

        Event expectedEvent1 = Event.today(t2, t7);
        Assert.assertEquals(expectedEvent1.getStart(), result.get(1).getStart());
        Assert.assertEquals(expectedEvent1.getEnd(), result.get(1).getEnd());

        Event expectedEvent2 = Event.today(t7, t9);
        Assert.assertEquals(expectedEvent2.getStart(), result.get(2).getStart());
        Assert.assertEquals(expectedEvent2.getEnd(), result.get(2).getEnd());
    }
}
