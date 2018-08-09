package de.kgrupp.inoksjavautils.resbundle;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.Collectors;

/**
 * @author Konstantin
 */
public abstract class IJUMessages {

    private final IJUResourceBundle<KeyProvider> resourceBundle;

    protected IJUMessages(String resourceName) {
        resourceBundle = new IJUResourceBundle<>(resourceName);
    }

    public final String get(String key, Object... args) {
        try {
            return MessageFormat.format(resourceBundle.getProperty(() -> key), args);
        } catch (MissingResourceException e) {
            List<String> params = Arrays.stream(args).map(String::valueOf).collect(Collectors.toList());
            return '[' + key + '(' + String.join(",", params) + ")]";
        }
    }
}
