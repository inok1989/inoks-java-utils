package de.kgrupp.inoksjavautils.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MultiValueMapTest {

    @Data
    @AllArgsConstructor
    private static class MyClass {

        private int key;
        private String value;
    }

    private static final MyClass V1 = new MyClass(1, "V1");
    private static final MyClass V2 = new MyClass(2, "V2");
    private static final MyClass V3 = new MyClass(1, "V3");
    private static final List<MyClass> LIST = Arrays.asList(V1, V2, V3);

    private MultiValueMap<Integer, MyClass, List<MyClass>> defaultMap;

    @BeforeEach
    void setUp() {
        defaultMap = new MultiValueMap<>(ArrayList::new);
        defaultMap.put(V1.getKey(), V1);
        defaultMap.put(V2.getKey(), V2);
        defaultMap.put(V3.getKey(), V3);
    }

    @Test
    void putAndGet() {
        assertDefaultValues(defaultMap);
    }

    @Test
    void listCollector() {
        final MultiValueMap<Integer, MyClass, List<MyClass>> collectedMap = LIST.stream().collect(MultiValueMap.listCollector(MyClass::getKey, Function.identity()));
        assertDefaultValues(collectedMap);
    }

    @Test
    void collector() {
        final MultiValueMap<Integer, MyClass, List<MyClass>> collectedMap = LIST.stream().collect(MultiValueMap.collector(MyClass::getKey, Function.identity(), () -> defaultMap));
        assertEquals(4, collectedMap.get(1).size());
        assertEquals(2, collectedMap.get(2).size());
        assertEquals(0, collectedMap.get(3).size());
    }

    @Test
    void collectorForParallelStream() {
        final MultiValueMap<Integer, MyClass, List<MyClass>> collectedMap = LIST.parallelStream().collect(MultiValueMap.collector(MyClass::getKey, Function.identity(), () -> new MultiValueMap<>(ArrayList::new)));
        assertDefaultValues(collectedMap);
    }

    private void assertDefaultValues(MultiValueMap<Integer, MyClass, List<MyClass>> map) {
        final List<MyClass> result1 = map.get(1);
        assertEquals(2, result1.size());
        assertTrue(result1.contains(V1));
        assertTrue(result1.contains(V3));

        final List<MyClass> result2 = map.get(2);
        assertEquals(1, result2.size());
        assertTrue(result2.contains(V2));

        assertTrue(map.get(3).isEmpty());
    }
}