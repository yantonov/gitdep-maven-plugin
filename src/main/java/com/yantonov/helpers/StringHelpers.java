package com.yantonov.helpers;

public class StringHelpers {

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }
}
