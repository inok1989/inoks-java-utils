package de.kgrupp.inoksjavautils.transform;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import de.kgrupp.monads.result.Result;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonUtilsTest {

    private static final MyClass EXAMPLE = new MyClass(1, 1.0);
    private static final String EXAMPLE_STRING = "{\"anInt\":1,\"someDouble\":1.0}";

    @Test
    void convertToJsonString() {
        Result<String> result = JsonUtils.convertToJsonString(EXAMPLE);
        assertEquals(Result.of(EXAMPLE_STRING), result);
    }

    @Test
    void convertToJsonStringFails() {
        final Result<String> result = JsonUtils.convertToJsonString(new Object() {
            // no fields and converting should fail
        });
        assertTrue(result.isInternalError());
        assertEquals(InvalidDefinitionException.class, result.getThrowable().getClass());
    }

    @Test
    void convertToObject() {
        final Result<MyClass> result = JsonUtils.convertToObject(EXAMPLE_STRING, MyClass.class);
        assertEquals(Result.of(EXAMPLE), result);
    }

    @Test
    void convertToObjectFail() {
        final Result<MyClass> result = JsonUtils.convertToObject("{", MyClass.class);
        assertTrue(result.isInternalError());
        assertEquals(JsonEOFException.class, result.getThrowable().getClass());
    }

    @Test
    void convertToObjectAsInputStream() {
        final Result<MyClass> result = JsonUtils.convertToObject(IOUtils.stringToInputStream(EXAMPLE_STRING).getObject(), MyClass.class);
        assertEquals(Result.of(EXAMPLE), result);
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

    private static class MyClass {
        private int anInt;
        private double someDouble;

        public MyClass() {
            // for serialization
        }

        public MyClass(int anInt, double someDouble) {
            this.anInt = anInt;
            this.someDouble = someDouble;
        }

        public int getAnInt() {
            return anInt;
        }

        public void setAnInt(int anInt) {
            this.anInt = anInt;
        }

        public double getSomeDouble() {
            return someDouble;
        }

        public void setSomeDouble(double someDouble) {
            this.someDouble = someDouble;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyClass myClass = (MyClass) o;
            return anInt == myClass.anInt &&
                    Double.compare(myClass.someDouble, someDouble) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(anInt, someDouble);
        }
    }
}