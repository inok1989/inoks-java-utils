package de.kgrupp.inoksjavautils.resbundle;

import java.time.LocalTime;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Konstantin
 */
public class IJUResourceBundle<T extends KeyProvider> {

    private static final Logger log = Logger.getLogger(IJUResourceBundle.class.getName());

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
