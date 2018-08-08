package de.kgrupp.inoksjavautils.constant;

import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Konstantin
 */
@Log
public abstract class AbstractConstant<T extends KeyProvider> {

    private final Properties properties;

    protected AbstractConstant(String resourcePath) {
        this.properties = new Properties();
        loadProperties(resourcePath);
    }

    private synchronized void loadProperties(String resourcePath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            properties.load(inputStream);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Loading properties failed", e);
        }
    }

    public String getProperty(T constant) {
        return properties.getProperty(constant.getKey());
    }

    public int getPropertyInt(T constant) {
        String object = properties.getProperty(constant.getKey());
        return Integer.parseInt(object);
    }

    public long getPropertyLong(T constant) {
        String object = properties.getProperty(constant.getKey());
        return Long.parseLong(object);
    }

    public boolean getPropertyBoolean(T constant) {
        String object = properties.getProperty(constant.getKey());
        return Boolean.parseBoolean(object);
    }

    public LocalTime getPropertyLocalTime(T constant) {
        String object = properties.getProperty(constant.getKey());
        return LocalTime.parse(object);
    }
}
