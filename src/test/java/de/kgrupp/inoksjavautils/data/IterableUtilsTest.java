package de.kgrupp.inoksjavautils.data;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IterableUtilsTest {

    private static final List<String> LIST = Arrays.asList("T1", "T2", "T3");

    @Test
    void getFirst() {
        final Optional<String> result = IterableUtils.getFirst(LIST);
        assertTrue(result.isPresent());
        assertEquals("T1", result.get());
    }

    @Test
    void getFirstOnEmptyList() {
        final Optional<String> result = IterableUtils.getFirst(Collections.emptyList());
        assertFalse(result.isPresent());
    }

}