package com.group20.dentanoid.Utils;

public class OSValidator { // Credits to: https://mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static String getOperatingSystem() {
        if (isWindows()) {
            return "Windows";
        } else if (isMac()) {
            return "Mac";
        } else if (isUnix()) {
            return "Unix/Linux";
        } else if (isSolaris()) {
            return "Solaris";
        } else {
            return "OS not supported";
        }
    }

    private static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    private static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    private static boolean isUnix() {
        return (OS.indexOf("nix") >= 0
                || OS.indexOf("nux") >= 0
                || OS.indexOf("aix") > 0);
    }

    private static boolean isSolaris() {
        return (OS.indexOf("sunos") >= 0);
    }
}