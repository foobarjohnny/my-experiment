package com.telenav.cserver.dsr.util;

import java.util.StringTokenizer;
import java.util.logging.Logger;

public class StrUtil {

    protected static Logger logger = Logger.getLogger(StrUtil.class.getName());

    public static boolean notBlank(String str) {
        return str != null && str.trim().length() > 0;
    }

    public static boolean isBlank(String str) {
        return !notBlank(str);
    }
    
    public static String firstLetterUppercase(String input) {
        return firstLetterUppercase(input, "");
    }

    public static String firstLetterUppercase(String input, String filter) {
        if (input == null || input.trim().length() == 0) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        StringTokenizer st = new StringTokenizer(input.toLowerCase());
        while (st.hasMoreTokens()) {
            String pe = st.nextToken();
            char[] chars = pe.toCharArray();
            if (pe.equals(filter)) {
                //don't change pe
            } else if (chars[0] >= 'a' && chars[0] <= 'z') {
                chars[0] = (char) (chars[0] - 32);
            }
            result.append(chars).append(" ");
        }
        //remove last space
        return result.substring(0, result.length() - 1);
    }
}
