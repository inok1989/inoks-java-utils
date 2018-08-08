package de.kgrupp.inoksjavautils;

import java.util.Iterator;
import java.util.Optional;

public final class IterableUtils {

    private IterableUtils() {
        // utility class
    }

    public static <T> Optional<T> getFirst(Iterable<T> iterable) {
        Iterator<T> iterator = iterable.iterator();
        if (iterator.hasNext()) {
            return Optional.of(iterator.next());
        } else {
            return Optional.empty();
        }
    }
}
