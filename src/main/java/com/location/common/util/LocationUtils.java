package com.location.common.util;


public final class LocationUtils {

    private LocationUtils() {
    }

    public static String normalizeName(String value) {

        if (value == null) {
            return null;
        }

        return value.trim();
    }

    public static String normalizeCode(String value) {

        if (value == null) {
            return null;
        }

        return value.trim().toUpperCase();
    }

    public static String normalizePincode(String value) {

        if (value == null) {
            return null;
        }

        return value.trim();
    }

}
