package de.kgrupp.inoksjavautils.transform;

import de.kgrupp.monads.result.Result;

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

    public static Result<String> inputStreamToString(InputStream inputStream) {
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return Result.of(result.toString(StandardCharsets.UTF_8.name()));
        } catch (IOException e) {
            return Result.fail("Converting InputStream to String failed.", e);
        }
    }

    public static Result<InputStream> stringToInputStream(String string) {
        try {
            return Result.of(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8.name())));
        } catch (UnsupportedEncodingException e) {
            return Result.fail(String.format("Converting String to InputStream failed for '%s'.", string), e);
        }
    }

}
