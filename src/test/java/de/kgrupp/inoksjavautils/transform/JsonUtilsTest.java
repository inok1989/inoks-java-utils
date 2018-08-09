package de.kgrupp.inoksjavautils.transform;

import de.kgrupp.inoksjavautils.exception.UnCheckedException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonUtilsTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class MyClass {
        private int anInt;
        private double someDouble;
    }

    private static final MyClass EXAMPLE = new MyClass(1, 1.0);
    private static final String EXAMPLE_STRING = "{\"anInt\":1,\"someDouble\":1.0}";

    @Test
    void convertToJsonString() {
        final String result = JsonUtils.convertToJsonString(EXAMPLE);
        assertEquals(EXAMPLE_STRING, result);
    }

    @Test
    void convertToJsonStringFails() {
        assertThrows(UnCheckedException.class, () -> JsonUtils.convertToJsonString(new Object() {
            // no fields and converting should fail
        }));
    }

    @Test
    void convertToObject() {
        final Optional<MyClass> result = JsonUtils.convertToObject(EXAMPLE_STRING, MyClass.class);
        assertTrue(result.isPresent());
        assertEquals(EXAMPLE, result.get());
    }

    @Test
    void convertToObjectFail() {
        assertThrows(UnCheckedException.class, () -> JsonUtils.convertToObject("{", MyClass.class));
    }

    @Test
    void convertToObjectAsInputStream() {
        final Optional<MyClass> result = JsonUtils.convertToObject(IOUtils.stringToInputStream(EXAMPLE_STRING), MyClass.class);
        assertTrue(result.isPresent());
        assertEquals(EXAMPLE, result.get());
    }

    @Test
    void convertToObjectUseDefaultOnError() {
        final MyClass result = JsonUtils.convertToObjectUseDefaultOnError(EXAMPLE_STRING, MyClass.class, null);
        assertEquals(EXAMPLE, result);
    }

    @Test
    void convertToObjectUseDefaultOnErrorDefaultUsed() {
        final MyClass result = JsonUtils.convertToObjectUseDefaultOnError("{", MyClass.class, EXAMPLE);
        assertEquals(EXAMPLE, result);
    }
}