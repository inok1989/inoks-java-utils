package de.kgrupp.inoksjavautils.resbundle;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.MissingResourceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IJUResourceBundleTest {

    static class ConstantTest extends IJUResourceBundle<KeyProvider> {

        protected ConstantTest() {
            super("de.kgrupp.inoksjavautils.resbundle.ConstantTest");
        }
    }

    private static final ConstantTest constantTest = new ConstantTest();

    @Test
    void getProperty() {
        String result = constantTest.getProperty(() -> "some.string");
        assertEquals("Blaub", result);
    }

    @Test
    void getPropertyInt() {
        int result = constantTest.getPropertyInt(() -> "some.int");
        assertEquals(123, result);
    }

    @Test
    void getPropertyLong() {
        long result = constantTest.getPropertyLong(() -> "some.long");
        assertEquals(456, result);
    }

    @Test
    void getPropertyBoolean() {
        boolean result = constantTest.getPropertyBoolean(() -> "some.boolean");
        assertTrue(result);
    }

    @Test
    void getPropertyLocalTime() {
        LocalTime result = constantTest.getPropertyLocalTime(() -> "some.time");
        assertEquals(LocalTime.of(12, 34, 56, 789123456), result);
    }

    @Test
    void loadingFails() {
        assertThrows(MissingResourceException.class, () -> new IJUResourceBundle<>("de.kgrupp.inoksjavautils.resbundle.missingFile"));
    }
}