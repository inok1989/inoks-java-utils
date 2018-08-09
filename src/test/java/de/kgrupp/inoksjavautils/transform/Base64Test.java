package de.kgrupp.inoksjavautils.transform;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Base64Test {

    private static final String PLAIN_TEXT = "lasdfjn329+...,-1!\"§$%&/()=??``";
    private static final byte[] PLAIN_BYTES = PLAIN_TEXT.getBytes();
    private static final String ENCODED_TEXT = "bGFzZGZqbjMyOSsuLi4sLTEhIsKnJCUmLygpPT8/YGA=";
    private static final byte[] ENCODED_BYTES = ENCODED_TEXT.getBytes();

    @Test
    void encodeString() {
        String encoded = Base64.encode(PLAIN_TEXT);
        assertEquals(ENCODED_TEXT, encoded);
    }

    @Test
    void decodeString() {
        String decoded = Base64.decode(ENCODED_TEXT);
        assertEquals(PLAIN_TEXT, decoded);
    }

    @Test
    void encodeBytes() {
        String encoded = Base64.encode(PLAIN_BYTES);
        assertEquals(ENCODED_TEXT, encoded);
    }

    @Test
    void decodeBytes() {
        String decoded = Base64.decode(ENCODED_BYTES);
        assertEquals(PLAIN_TEXT, decoded);
    }

    @Test
    void encodeToBytes() {
        byte[] encoded = Base64.encodeToBytes(PLAIN_TEXT);
        assertByteArray(ENCODED_BYTES, encoded);
    }

    @Test
    void decodeToBytes() {
        byte[] decoded = Base64.decodeToBytes(ENCODED_TEXT);
        assertByteArray(PLAIN_BYTES, decoded);
    }

    private void assertByteArray(byte[] expected, byte[] actual) {
        for (int i = 0; i < expected.length; i += 1) {
            assertEquals(expected[i], actual[i]);
        }
    }
}