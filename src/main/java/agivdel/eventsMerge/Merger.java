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

// обрати внимание, у тебя тут остались только чистые функции, состояния нет
// это значит, мы можем сделать все методы static и обойтись без объектов класса Merger
// не всегда это приемлемо, но в нашем случае - ок
// в целом же помомо косяка с сортировкой результат неплох
// но я бы не стал выделять getMergedSet как отдельнцю функцию
public class Merger {

    public Set<Event> setToMerge(Set<Event> eventSet) {
        checkEventSetIsNotNull(eventSet);
        if (eventSet.isEmpty() || eventSet.size() == 1) {
            return eventSet;
        }
        List<Event> eventList = new ArrayList<>(eventSet);
        // еще раз подумал - так проверять нельзя
        // потому что ты не можешь гарантировать, что наш SortedSet
        // будет использовать сортировку по умолчанию (agivdel.eventsMerge.Event.compareTo)
        // там может быть любой компаратор
        // а посему сортировать нужно здесь
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
                & e1.getEnd().isAfter(e2.getStart()); //не используй единичный & без явной нужды, всегда юзай двойной && - https://ru.stackoverflow.com/questions/938756/%D0%92-%D1%87%D0%B5%D0%BC-%D1%80%D0%B0%D0%B7%D0%BD%D0%B8%D1%86%D0%B0-%D0%BC%D0%B5%D0%B6%D0%B4%D1%83-%D0%B8-%D0%BC%D0%B5%D0%B6%D0%B4%D1%83-java
    }

    //стало лучше
    //сейчас метод защищен от неправильного использования приватностью
    //раньше в него можно было передать непересекающиеся ивенты - и он бы выдал хуйню
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
