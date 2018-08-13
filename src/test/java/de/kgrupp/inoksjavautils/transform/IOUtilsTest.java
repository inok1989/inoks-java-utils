package de.kgrupp.inoksjavautils.transform;

import de.kgrupp.monads.result.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IOUtilsTest {

    private InputStream stream;
    private InputStream failingStream;
    private static final String STRING = "023SDFjf20lksä.!\"§$%&/()=?```";

    @BeforeEach
    void setUp() {
        stream = new InputStream() {
            private int count = 0;

            @Override
            public int read() {
                if (count < STRING.getBytes().length) {
                    byte nextValue = STRING.getBytes()[count];
                    count += 1;
                    return nextValue;
                } else {
                    return -1;
                }
            }
        };
        failingStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("JUST FAILS");
            }
        };
    }

    @Test
    void inputStreamToString() {
        Result<String> result = IOUtils.inputStreamToString(stream);
        assertEquals(Result.of(STRING), result);
    }

    @Test
    void stringToInputStream() throws IOException {
        Result<InputStream> result = IOUtils.stringToInputStream(STRING);
        assertTrue(result.isSuccess());
        while (true) {
            int actual = result.getObject().read();
            int expect = stream.read();
            assertEquals(expect, actual);
            if (0 <= expect) {
                break;
            }
        }
    }

    @Test
    void inputStreamToStringFails() {
        Result<String> result = IOUtils.inputStreamToString(failingStream);
        assertTrue(result.isInternalError());
    }
}