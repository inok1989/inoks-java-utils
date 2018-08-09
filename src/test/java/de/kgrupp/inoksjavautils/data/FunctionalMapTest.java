package de.kgrupp.inoksjavautils.data;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FunctionalMapTest {

    @Test
    void putAndGet() {
        FunctionalMap<Integer, String> functionalMap = new FunctionalMap<>("START-");
        functionalMap.put(1, "TEST1-", String::concat);
        functionalMap.put(2, "TEST2", String::concat);
        functionalMap.put(1, "TEST3", String::concat);
        String result1 = functionalMap.get(1);
        assertEquals("START-TEST1-TEST3", result1);

        String result2 = functionalMap.get(2);
        assertEquals("START-TEST2", result2);

        String result3 = functionalMap.get(3);
        assertEquals("START-", result3);
    }

    @Test
    void stream() {
        FunctionalMap<Double, String> functionalMap = new FunctionalMap<>("");
        functionalMap.put(1.0D, "TEST-ONE", String::concat);
        functionalMap.put(2.0D, "TEST-TWO", String::concat);
        Stream<String> stream = functionalMap.stream((key, value) -> "[" + key + ", " + value + "]");
        List<String> list = stream.collect(Collectors.toList());
        assertEquals("[1.0, TEST-ONE]", list.get(0));
        assertEquals("[2.0, TEST-TWO]", list.get(1));
    }

    @Test
    void streamEmpty() {
        FunctionalMap<Integer, String> functionalMap = new FunctionalMap<>("");
        Stream<String> stream = functionalMap.stream((key, value) -> "TEST");
        assertTrue(stream.collect(Collectors.toList()).isEmpty());
    }
}