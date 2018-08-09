package de.kgrupp.inoksjavautils.resbundle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IJUMessagesTest {

    private static class MessagesTest extends IJUMessages {

        protected MessagesTest() {
            super("de.kgrupp.inoksjavautils.resbundle.MessagesTest");
        }
    }

    private static final MessagesTest MESSAGES = new MessagesTest();

    @Test
    void get() {
        assertEquals("SOME_VALUE", MESSAGES.get("test.property"));
    }

    @Test
    void getWithParam() {
        assertEquals("SOME_SOME_PARAM_AND_ANOTHER_VALUE", MESSAGES.get("test.property.param", "SOME_PARAM", "AND_ANOTHER"));
    }

    @Test
    void getMissing() {
        assertEquals("[test.property.missing(SOME_PARAM)]", MESSAGES.get("test.property.missing", "SOME_PARAM"));
    }
}