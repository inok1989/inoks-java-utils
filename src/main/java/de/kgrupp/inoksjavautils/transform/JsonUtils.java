package de.kgrupp.inoksjavautils.transform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.kgrupp.monads.result.Result;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public final class JsonUtils {

    private static final Logger log = Logger.getLogger(JsonUtils.class.getName());

    private JsonUtils() {
        // utility class
    }

    public static Result<String> convertToJsonString(Object object) {
        ObjectMapper mapper = getObjectMapper();
        try {
            return Result.of(mapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            return Result.fail(String.format("Converting object to json string failed for '%s'", object), e);
        }
    }

    public static <T> Result<T> convertToObject(InputStream jsonInputStream, Class<T> clazz) {
        return IOUtils.inputStreamToString(jsonInputStream).flatMap(jsonString -> convertToObject(jsonString, clazz));
    }

    public static <T> Result<T> convertToObject(String jsonString, Class<T> clazz) {
        ObjectMapper mapper = getObjectMapper();
        try {
            return Result.of(mapper.readValue(jsonString, clazz));
        } catch (IOException e) {
            return Result.fail(String.format("Converting json to object %s failed for '%s'.", clazz.getName(), jsonString), e);
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
        return new ObjectMapper().registerModules(new JodaModule(), new JavaTimeModule());
    }

}
