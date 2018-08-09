package de.kgrupp.inoksjavautils.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class MultiValueMap<K, V, C extends Collection<V>> {

    private HashMap<K, C> map = new HashMap<>();
    private Supplier<C> builder;

    public MultiValueMap(Supplier<C> builder) {
        this.builder = builder;
    }

    public void put(K key, V value) {
        map.compute(key, (key1, oldValue) -> {
            if (oldValue == null) {
                oldValue = builder.get();
            }
            oldValue.add(value);
            return oldValue;
        });
    }

    public @NotNull C get(K key) {
        return map.computeIfAbsent(key, key1 -> builder.get());
    }

    public @NotNull Collection<Map.Entry<K, C>> entrySet() {
        return map.entrySet();
    }

    public static <T, K, V> Collector<T, MultiValueMap<K, V, List<V>>, MultiValueMap<K, V, List<V>>> listCollector(Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return collector(keyMapper, valueMapper, () -> new MultiValueMap<>(ArrayList::new));
    }

    public static <M extends MultiValueMap<K, V, C>, T, K, V, C extends Collection<V>> Collector<T, M, M> collector(Function<T, K> keyMapper,
                                                                                                                    Function<T, V> valueMapper,
                                                                                                                    Supplier<M> mapSupplier) {
        BiConsumer<M, T> accumulator = (map, element) -> map.put(keyMapper.apply(element), valueMapper.apply(element));
        return Collector.of(mapSupplier, accumulator, merger(mapSupplier));
    }

    private static <K, V, C extends Collection<V>, M extends MultiValueMap<K, V, C>>
    BinaryOperator<M> merger(Supplier<M> mapSupplier) {
        return (m1, m2) -> {
            M result = mapSupplier.get();
            m1.entrySet().forEach(entry -> entry.getValue().forEach(value -> result.put(entry.getKey(), value)));
            m2.entrySet().forEach(entry -> entry.getValue().forEach(value -> result.put(entry.getKey(), value)));
            return result;
        };
    }
}
