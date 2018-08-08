package de.kgrupp.inoksjavautils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import de.kgrupp.inoksjavautils.exception.UnCheckedException;
import de.kgrupp.inoksjavautils.io.IOUtils;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Log
public final class JsonUtils {

    private JsonUtils() {
        // utility class
    }

    public static String convertToJsonString(Object object) {
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new UnCheckedException(e);
        }
    }

    public static <T> Optional<T> convertToObject(InputStream jsonInputStream, Class<T> clazz) {
        return convertToObject(IOUtils.inputStreamToString(jsonInputStream), clazz);
    }

    public static <T> Optional<T> convertToObject(String jsonString, Class<T> clazz) {
        ObjectMapper mapper = getObjectMapper();
        try {
            return Optional.ofNullable(mapper.readValue(jsonString, clazz));
        } catch (IOException e) {
            throw new UnCheckedException(e);
        }
    }

    public static <T> T convertToObjectUseDefaultOnError(String jsonString, Class<T> clazz, T defaultValue) {
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            log.warning(String.format("JSON '%s' could not be parsed to object of type '%s'. Default value '%s' will be used.",
                    jsonString,
                    clazz.getSimpleName(),
                    defaultValue));
            return defaultValue;
        }
    }

    public static ObjectMapper getObjectMapper() {
        return new ObjectMapper().registerModules(new JodaModule(), new JSR310Module());
    }

}
