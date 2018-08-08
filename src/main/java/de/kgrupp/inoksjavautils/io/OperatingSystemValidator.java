package de.kgrupp.inoksjavautils.io;

/**
 * https://stackoverflow.com/questions/14288185/detecting-windows-or-linux
 * Code from: https://gist.github.com/kiuz/816e24aa787c2d102dd0
 */
public final class OperatingSystemValidator {

    private static final String OPERATING_SYSTEM = System.getProperty("os.name").toLowerCase();

    private OperatingSystemValidator() {
        // utility class
    }

    public static boolean isWindows() {
        return (OPERATING_SYSTEM.contains("win"));
    }

    public static boolean isMac() {
        return (OPERATING_SYSTEM.contains("mac"));
    }

    public static boolean isUnix() {
        return (OPERATING_SYSTEM.contains("nix") || OPERATING_SYSTEM.contains("nux") || OPERATING_SYSTEM.indexOf("aix") > 0 );
    }

    public static boolean isSolaris() {
        return (OPERATING_SYSTEM.contains("sunos"));
    }

    public static String getOperatingSystem(){
        if (isWindows()) {
            return "win";
        } else if (isMac()) {
            return "osx";
        } else if (isUnix()) {
            return "uni";
        } else if (isSolaris()) {
            return "sol";
        } else {
            return "err";
        }
    }

}
