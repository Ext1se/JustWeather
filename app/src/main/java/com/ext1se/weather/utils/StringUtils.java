package com.ext1se.weather.utils;

public class StringUtils {
    public static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
