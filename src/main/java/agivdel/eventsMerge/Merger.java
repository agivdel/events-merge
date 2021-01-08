package agivdel.eventsMerge;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс с алгоритмом объединения событий в случе их пересечения
 * Поля объекта Merger - два объекта Event, absorbed и absorber.
 * В каждой проверке событий на пересечение и в каждом объединении участвуют оба поля.
 * Если два события только соприкасаются своими границами
 * (например, время окончания одного равно времени начала другого), объединение не происходит.
 * Метод setToMerge() возвращает множество событий, не перекрываемых друг другом.
 * Если на вход метода setToMerge() подано множество не класса TreeSet, перед анализом событий оно сортируется.
 * Множество на входе метода setToMerge() не должно быть равным null или пустым.
 * Если множество на входе метода setToMerge() состоит из одного элемента (равного null или нет),
 * метод возвращает данное множество без изменений.
 */


public class Merger {

    //Твой класс мог бы иметь единственную функцию, а у тебя тут состояние появилось
    //Методы, которые использую эти поля, могли бы принимать их как параметры
    Event absorbed;
    Event absorber;

    public Merger() {
    }

    public Merger(Event event1, Event event2) {
        this.absorbed = event1;
        this.absorber = event2;
    }

    //значения из списка либо не меняем, либо переводим в null и перезаписываем, перед выдачей убираем все null
    public Set<Event> setToMerge(Set<Event> eventSet) {
        checkEventSetIsNotNull(eventSet);  // Обсуждаемо. Мож и невалидно, а мож и эквивалентно пустому.
        checkEventSetIsNotEmpty(eventSet); // Ненуачо? Пустое множество вполне валидно. Резальтат очевиден - тож пустое множество.
        if (eventSet.size() == 1) {
            return eventSet;
        }
        // алгоритм в целом понятен, но не изящен
        List<Event> eventList = new ArrayList<>(eventSet);
        // оптимизация не покрывает все возможные варианты
        // лучше так: if (!(eventSet instanceof SortedSet))
        // ну и ваще тогда можно сразу принимать SortedSet как параметр метода
        if (eventSet.getClass() != TreeSet.class) {
            Collections.sort(eventList);
        }
        for (int i = 0; i < eventList.size() - 1; i++) {
            absorbed = eventList.get(i);
            absorber = eventList.get(i + 1); //ты редактируешь параметр метода - это делает твою функцию нечистой, не надо так
            if (isOverlap()) {
                merge();
                eventList.set(i, null);
                eventList.set(i + 1, absorber);//перезаписываем элемент - с расширенными границами
            }
        }
        return eventList.stream().filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public void checkEventSetIsNotNull(Set<Event> eventSet) {
        if (eventSet == null) {
            throw new IllegalArgumentException("The eventSet can't be null.");
        }
    }

    public void checkEventSetIsNotEmpty(Set<Event> eventSet) {
        if (eventSet.isEmpty()) {
            throw new IllegalArgumentException("The eventSet can't be empty.");
        }
    }

    //если границы событий соприкасаются, они не перекрываются
    public boolean isOverlap() {
        return absorbed.getStart().isBefore(absorber.getEnd())
                & absorbed.getEnd().isAfter(absorber.getStart());
    }

    public void merge() {
        LocalDateTime start = getPrevious(absorber.getStart(), absorbed.getStart());
        absorber.setStart(start);
        LocalDateTime end = getSubsequent(absorber.getEnd(), absorbed.getEnd());
        absorber.setEnd(end);
    }

    public LocalDateTime getPrevious(LocalDateTime ldt1, LocalDateTime ldt2) {
        return ldt1.isBefore(ldt2) ? ldt1 : ldt2;
    }

    public LocalDateTime getSubsequent(LocalDateTime ldt1, LocalDateTime ldt2) {
        return ldt1.isAfter(ldt2) ? ldt1 : ldt2;
    }
}
