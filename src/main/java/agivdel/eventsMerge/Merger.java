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
    //АГ: статические методы менее (реже) опасны, чем статические переменные?
    //Пора тебе, барин, познакомиться с моками / стабами.
    //Проблема статического метода в том, что его вызов нельзя (можно, но сложно), подменить в тестах.
    //Однако статический метод можно и не использовать напрямую, него частенько можно сослаться как на функциональный интерфейс
// в целом же помомо косяка с сортировкой результат неплох
// но я бы не стал выделять getMergedSet как отдельнцю функцию
    //АГ: а чем это плохо? Всего лишь валидация и сортировка отделены от анализа и слияния...
    //Вот сортировку я бы убрал как раз в отдельный метод
    //Ладно, тут больше вкусовщина
public class Merger {

    public Set<Event> setToMerge(Set<Event> eventSet) {
        checkEventSetIsNotNull(eventSet);
        //кста, на проверял, но эту проверку можно удалить и ничо не поменяется. ну почти ничего
        if (eventSet.isEmpty() || eventSet.size() == 1) {
            return eventSet;
        }
        //сортируем каждое входящее множество
        List<Event> eventList = eventSet.stream().filter(Objects::nonNull).sorted().collect(Collectors.toList());
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
                && e1.getEnd().isAfter(e2.getStart());
        //не используй единичный & без явной нужды, всегда юзай двойной &&
        //АГ: разницу между умножением и суммой я знаю. Просто ступил.
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
