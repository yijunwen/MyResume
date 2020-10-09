package com.yhaj.xr.util;

/**
 * @author yhaj
 * @version 1.0
 * @date 2020/10/6 10:03
 */
public class Strings {
    public static String underlineCase(String str) {
        if (str == null) return null;
        int length = str.length();
        if (length == 0) return str;
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(str.charAt(0)));
        for (int i = 1; i < length; i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_")
                        .append(Character.toLowerCase(c));
            } else
                sb.append(c);
        }
        return sb.toString();
    }
}
