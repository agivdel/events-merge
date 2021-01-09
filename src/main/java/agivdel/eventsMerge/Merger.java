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

    //Твой класс мог бы иметь единственную функцию, а у тебя тут состояние появилось
    //Методы, которые использую эти поля, могли бы принимать их как параметры
//    Event absorbed;
//    Event absorber;


    //значения из списка либо не меняем, либо переводим в null и перезаписываем, перед выдачей убираем все null
    public Set<Event> setToMerge(Set<Event> eventSet) {
        checkEventSetIsNotNull(eventSet);  // Обсуждаемо. Мож и невалидно, а мож и эквивалентно пустому.
        //checkEventSetIsNotEmpty(eventSet); // Ненуачо? Пустое множество вполне валидно. Резальтат очевиден - тож пустое множество.
        //АГ: убрал, блин...
        if (eventSet.isEmpty() || eventSet.size() == 1) {
            return eventSet;
        }
        // алгоритм в целом понятен, но не изящен
        List<Event> eventList = new ArrayList<>(eventSet);
        // оптимизация не покрывает все возможные варианты
        // лучше так: if (!(eventSet instanceof SortedSet))
        // ну и ваще тогда можно сразу принимать SortedSet как параметр метода
//        if (eventSet.getClass() != TreeSet.class) {
//            Collections.sort(eventList);
//        }
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
            Event e2 = list.get(i + 1); //ты редактируешь параметр метода - это делает твою функцию нечистой, не надо так
            //АГ: не понял...
            if (isOverlap(e1, e2)) {
                e2 = mergeOf(e1, e2);
                list.set(i, null);
                list.set(i + 1, e2);//перезаписываем элемент - с расширенными границами
            }
        }
        return list.stream().filter(Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new));
    }



/*    public void checkEventSetIsNotEmpty(Set<Event> eventSet) {
        if (eventSet.isEmpty()) {
            throw new IllegalArgumentException("The eventSet can't be empty.");
        }
    }*/

    //если границы событий соприкасаются, они не перекрываются
    public boolean isOverlap(Event e1, Event e2) {
        return e1.getStart().isBefore(e2.getEnd())
                & e1.getEnd().isAfter(e2.getStart());
    }

    public Event mergeOf(Event e1, Event e2) {
        LocalDateTime start = getEarlier(e2.getStart(), e1.getStart());
        LocalDateTime end = getLater(e2.getEnd(), e1.getEnd());
        return new Event(start, end);
    }

    public LocalDateTime getEarlier(LocalDateTime ldt1, LocalDateTime ldt2) {
        return ldt1.isBefore(ldt2) ? ldt1 : ldt2;
    }

    public LocalDateTime getLater(LocalDateTime ldt1, LocalDateTime ldt2) {
        return ldt1.isAfter(ldt2) ? ldt1 : ldt2;
    }



    //если границы событий соприкасаются, они не перекрываются
    public static boolean isOverlapStatic(Event e1, Event e2) {
        return e1.getStart().isBefore(e2.getEnd())
                & e1.getEnd().isAfter(e2.getStart());
    }

    public static Event mergeStatic(Event e1, Event e2) {
        LocalDateTime start = e1.getStart().isBefore(e2.getStart()) ? e1.getStart() : e2.getStart();
        LocalDateTime end = e1.getEnd().isAfter(e2.getEnd()) ? e1.getEnd() : e2.getEnd();
        return new Event(start, end);
    }
}
