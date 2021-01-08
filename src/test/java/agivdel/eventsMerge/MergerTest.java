package agivdel.eventsMerge;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class MergerTest {
    Merger merger;

    private final LocalTime lt1000 = LocalTime.of(10, 0);
    private final LocalTime lt1030 = LocalTime.of(10, 30);
    private final LocalTime lt1100 = LocalTime.of(11, 0);
    private final LocalTime lt1130 = LocalTime.of(11, 30);
    private final LocalTime lt1200 = LocalTime.of(12, 0);
    private final LocalTime lt1230 = LocalTime.of(12, 30);
    private final LocalTime lt1300 = LocalTime.of(13, 0);
    private final LocalTime lt1330 = LocalTime.of(13, 30);
    private final LocalTime lt1400 = LocalTime.of(14, 0);

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void beforeTest() {
        merger = new Merger();
    }

    @Test
    public void isOverlapTest() {
        merger.absorbed = new Event(lt1030, lt1100);

        merger.absorber = new Event(lt1030, lt1200);
        Assert.assertTrue(merger.isOverlap());

        merger.absorber = new Event(lt1000, lt1130);
        Assert.assertTrue(merger.isOverlap());
    }

    @Test
    public void isNotOverlapTest() {
        merger.absorbed = new Event(lt1030, lt1100);

        merger.absorber = new Event(lt1100, lt1400);
        Assert.assertFalse(merger.isOverlap());

        merger.absorber = new Event(lt1000, lt1030);
        Assert.assertFalse(merger.isOverlap());
    }

    @Test
    public void mergeTest() {
        Event event1 = new Event(lt1000, lt1400);
        Event event2 = new Event(lt1130, lt1330);
        merger = new Merger(event1, event2);

        merger.merge();
        Assert.assertEquals(lt1000, merger.absorber.getStart().toLocalTime());
        Assert.assertEquals(lt1400, merger.absorber.getEnd().toLocalTime());
    }

    @Test
    public void setToMergeWithNullSetTest() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The eventSet can't be null.");
        merger.setToMerge(null);
    }

    @Test
    public void setToMergeWithEmptySetTest() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The eventSet can't be empty.");
        merger.setToMerge(new HashSet<>());
        merger.setToMerge(new TreeSet<>());
        merger.setToMerge(new LinkedHashSet<>());
    }

    @Test
    public void hashSetToMergeWithOnlyNullElementTest() {
        Set<Event> eventHashSet0 = new HashSet<>();
        eventHashSet0.add(null);

        Set<Event> eventSet = merger.setToMerge(eventHashSet0);
        Assert.assertEquals(eventSet, eventHashSet0);
    }

    @Test
    public void linkedHashSetToMergeWithOnlyNullElementTest() {
        Set<Event> eventLinkedHashSet0 = new LinkedHashSet<>();
        eventLinkedHashSet0.add(null);

        Set<Event> eventSet = merger.setToMerge(eventLinkedHashSet0);
        Assert.assertEquals(eventSet, eventLinkedHashSet0);
    }

    @Test
    public void hashSetToMergeWithOnlyNotNullElementTest() {
        Set<Event> eventHashSet0 = new HashSet<>();
        eventHashSet0.add(new Event(lt1000, lt1030));

        Set<Event> eventHashSet1 = merger.setToMerge(eventHashSet0);
        Assert.assertEquals(eventHashSet0, eventHashSet1);
    }

    @Test
    public void treeSetToMergeWithOnlyNotNullElementTest() {
        Set<Event> eventTreeSet0 = new TreeSet<>();
        eventTreeSet0.add(new Event(lt1000, lt1030));

        Set<Event> eventTreeSet1 = merger.setToMerge(eventTreeSet0);
        Assert.assertEquals(eventTreeSet0, eventTreeSet1);
    }

    @Test
    public void linkedHashSetToMergeWithOnlyNotNullElementTest() {
        Set<Event> eventLinkedHashSet0 = new LinkedHashSet<>();
        eventLinkedHashSet0.add(new Event(lt1000, lt1030));

        Set<Event> eventLinkedHashSet1 = merger.setToMerge(eventLinkedHashSet0);
        Assert.assertEquals(eventLinkedHashSet0, eventLinkedHashSet1);
    }

    @Test
    public void setToMergeWithCorrectSetTest() {
        Set<Event> eventSet = getEventSet();
        System.out.println(eventSet);

        Set<Event> mergedSet2 = merger.setToMerge(eventSet);
        Assert.assertEquals(3, mergedSet2.size());
    }

    private Set<Event> getEventSet() {
        Event event1 = new Event(lt1000, lt1030);
        Event event2 = new Event(lt1030, lt1330);
        Event event3 = new Event(lt1230, lt1300);
        Event event4 = new Event(lt1330, lt1400);

        Set<Event> eventSet = new TreeSet<>();
        eventSet.add(event4);
        eventSet.add(event1);
        eventSet.add(event3);
        eventSet.add(event2);

        return eventSet;
    }
}
