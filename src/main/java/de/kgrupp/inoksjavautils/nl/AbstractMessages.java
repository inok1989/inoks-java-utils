package de.kgrupp.inoksjavautils.nl;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author Konstantin
 */
public abstract class AbstractMessages {

    private final ResourceBundle resourceBundle;

    protected AbstractMessages(String resourceName) {
        resourceBundle = ResourceBundle.getBundle(resourceName);
    }

    public final String get(String key, Object... args) {
        try {
            return MessageFormat.format(resourceBundle.getString(key), args);
        } catch (MissingResourceException e) {
            List<String> params = Arrays.stream(args).map(String::valueOf).collect(Collectors.toList());
            return '[' + key + '(' + String.join(",", params) + ")]";
        }
    }
}
