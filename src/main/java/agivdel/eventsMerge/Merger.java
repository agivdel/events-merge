package agivdel.eventsMerge;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс с алгоритмом объединения событий в случе их пересечения.
 * Если два события только соприкасаются своими границами
 * (например, время окончания одного равно времени начала другого), объединение не происходит.
 * Метод setToMerge() возвращает сортированное множество событий, не перекрываемых друг другом.
 * Если на вход метода setToMerge() подано не SortedSet множество, перед анализом оно сортируется.
 * Множество на входе метода setToMerge() не должно быть равным null.
 * Если множество на входе метода setToMerge() пустое или состоит из одного элемента (равного null или нет),
 * метод возвращает данное множество без изменений.
 */


public class Merger {

    public Set<Event> setToMerge(Set<Event> eventSet) {
        checkEventSetIsNotNull(eventSet);
        if (eventSet.isEmpty() || eventSet.size() == 1) {
            return eventSet;
        }
        List<Event> eventList = new ArrayList<>(eventSet);
        if (!(eventSet instanceof SortedSet)) {
            Collections.sort(eventList);
        }
        return getMergedSet(eventList);
    }

    private void checkEventSetIsNotNull(Set<Event> eventSet) {
        if (eventSet == null) {
            throw new IllegalArgumentException("The eventSet can't be null.");
        }
    }

    private Set<Event> getMergedSet(List<Event> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            Event e1 = list.get(i);
            Event e2 = list.get(i + 1);
            if (isOverlap(e1, e2)) {
                e2 = mergeOf(e1, e2);
                list.set(i, null);
                list.set(i + 1, e2);
            }
        }
        return list.stream().filter(Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private boolean isOverlap(Event e1, Event e2) {
        return e1.getStart().isBefore(e2.getEnd())
                & e1.getEnd().isAfter(e2.getStart());
    }

    private Event mergeOf(Event e1, Event e2) {
        LocalDateTime start = getEarlier(e2.getStart(), e1.getStart());
        LocalDateTime end = getLater(e2.getEnd(), e1.getEnd());
        return new Event(start, end);
    }

    private LocalDateTime getEarlier(LocalDateTime ldt1, LocalDateTime ldt2) {
        return ldt1.isBefore(ldt2) ? ldt1 : ldt2;
    }

    private LocalDateTime getLater(LocalDateTime ldt1, LocalDateTime ldt2) {
        return ldt1.isAfter(ldt2) ? ldt1 : ldt2;
    }
}
