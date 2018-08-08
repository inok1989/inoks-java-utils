package de.kgrupp.inoksjavautils.data;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class FunctionalMap<K, V> {

    private final Map<K, V> map = new HashMap<>();
    private final V defaultValue;

    public FunctionalMap(V defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void put(K key, V value, BiFunction<V, V, V> function) {
        V oldValue = map.get(key);
        if (oldValue == null) {
            oldValue = defaultValue;
        }
        V newValue = function.apply(oldValue, value);
        map.put(key, newValue);
    }

    @NotNull
    public V get(K key) {
        V value = map.get(key);
        return value != null ? value : defaultValue;
    }

    public <B> Stream<B> stream(BiFunction<K, V, B> merger) {
        return map.entrySet().stream().map(entry -> merger.apply(entry.getKey(), entry.getValue()));
    }
}
