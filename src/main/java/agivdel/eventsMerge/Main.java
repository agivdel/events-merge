package agivdel.eventsMerge;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static java.time.LocalTime.*;
import java.util.*;

/**
 * Есть расписание. Каждое событие в расписании имеет время начала и время конца. Пересечения событий допускаются.
 * Нужно найти пересекающиеся события и слить их в единые события.
 * (Например, 2 события: Первое с часу до четырех. Второе с двух до пяти. Результат - с часу до пяти.)
 * Нужен класс с имплементацией алгоритма слияния и тесты.
 * <p>
 * Работаем со списком событий. Элемент списка - объект класса Event с двумя полями, для начала и конца события.
 * Поле объекта Event - объект класса LocalDateTime.
 * Получаем список событий, применяем к нему алгоритм слияния в классе Merge..
 */

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        Set<Event> eventSet = getEventSet(
                of(15, 0), of(15, 30),
                of(15, 20), of(16, 30),
                of(17, 40), of(17, 45),
                of(17, 20), of(18, 0),
                of(19, 20));

        System.out.println("before events merge: ");
        eventSet.forEach(System.out::println);
        System.out.println(eventSet.size());
        Set<Event> newSet = new Merger().setToMerge(eventSet);
        System.out.println("after merge: ");
        newSet.forEach(System.out::println);

    }

    private static Set<Event> getEventSet(LocalTime... localTimes) {
        Set<Event> eventSet = new TreeSet<>();
        Event event;
        for (int i = 0; i < localTimes.length - 1; i += 2) {
            event = new Event(localTimes[i], localTimes[i + 1]);
            eventSet.add(event);
        }
        return eventSet;
    }
}
