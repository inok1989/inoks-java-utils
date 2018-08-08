package de.kgrupp.inoksjavautils;

import java.nio.charset.StandardCharsets;

public final class Base64 {

    private Base64() {
        // utility class
    }

    public static String encode(String plainText) {
        byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
        return encode(plainBytes);
    }

    public static String encode(byte[] plainBytes) {
        return new String(encodeToBytes(plainBytes), StandardCharsets.UTF_8);
    }

    public static byte[] encodeToBytes(String plainText) {
        byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
        return encodeToBytes(plainBytes);
    }

    private static byte[] encodeToBytes(byte[] plainBytes) {
        return java.util.Base64.getEncoder().encode(plainBytes);
    }

    public static String decode(String encoded) {
        byte[] encodedBytes = encoded.getBytes(StandardCharsets.UTF_8);
        return decode(encodedBytes);
    }

    public static String decode(byte[] encodedBytes) {
        return new String(decodeToBytes(encodedBytes), StandardCharsets.UTF_8);
    }

    public static byte[] decodeToBytes(String encoded) {
        byte[] encodedBytes = encoded.getBytes(StandardCharsets.UTF_8);
        return decodeToBytes(encodedBytes);
    }

    private static byte[] decodeToBytes(byte[] encodedBytes) {
        return java.util.Base64.getDecoder().decode(encodedBytes);
    }

}
