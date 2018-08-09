package de.kgrupp.inoksjavautils.resbundle;

import lombok.extern.java.Log;

import java.time.LocalTime;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;

/**
 * @author Konstantin
 */
@Log
public class IJUResourceBundle<T extends KeyProvider> {

    private final ResourceBundle resourceBundle;

    public IJUResourceBundle(String resourceName) {
        try {
            resourceBundle = ResourceBundle.getBundle(resourceName);
        } catch (MissingResourceException e) {
            log.log(Level.SEVERE, "Loading properties failed", e);
            throw e;
        }
    }

    private String get(T constant) {
        return resourceBundle.getString(constant.getKey());
    }

    public String getProperty(T constant) {
        return get(constant);
    }

    public int getPropertyInt(T constant) {
        String object = get(constant);
        return Integer.parseInt(object);
    }

    public long getPropertyLong(T constant) {
        String object = get(constant);
        return Long.parseLong(object);
    }

    public boolean getPropertyBoolean(T constant) {
        String object = get(constant);
        return Boolean.parseBoolean(object);
    }

    public LocalTime getPropertyLocalTime(T constant) {
        String object = get(constant);
        return LocalTime.parse(object);
    }
}
