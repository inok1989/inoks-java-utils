package de.kgrupp.inoksjavautils.io;

import de.kgrupp.inoksjavautils.exception.UnCheckedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public final class IOUtils {

    private IOUtils() {
        // utility class
    }

    public static String inputStreamToString(InputStream inputStream) {
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new UnCheckedException(e);
        }
    }

    public static InputStream stringToInputStream(String string) {
        try {
            return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            throw new UnCheckedException(e);
        }
    }

}
