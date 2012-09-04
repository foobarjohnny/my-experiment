package com.telenav.cserver.dsr.util;

import java.lang.reflect.Method;

/**
 * User: llzhang
 * Date: 5/24/11
 * Time: 7:13 PM
 * Telenav, CO.
 */
public class FieldCopier {
    public static void copy(Object from, Object to) throws Exception {
        Method[] methods = to.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                String getMethodName = method.getName().replace("set", "get");
                Object value = getMethodValue(from, getMethodName);
                if (value == null) {
                    continue;
                }
                if (sameClass(value.getClass(), method.getParameterTypes()[0])) {
                    method.invoke(to, value);
                }
            }
        }
    }

    private static boolean sameClass(Class klassA, Class klassB) {
        if (klassA.equals(klassB)) {
            return true;
        }
        if (klassA.equals(int.class)) {
            return klassB.equals(Integer.class);
        }
        if (klassA.equals(Integer.class)) {
            return klassB.equals(int.class);
        }
        if (klassA.equals(long.class)) {
            return klassB.equals(Long.class);
        }
        if (klassA.equals(Long.class)) {
            return klassB.equals(long.class);
        }
        if (klassA.equals(boolean.class)) {
            return klassB.equals(Boolean.class);
        }
        if (klassA.equals(Boolean.class)) {
            return klassB.equals(boolean.class);
        }
        return false;
    }

    private static Object getMethodValue(Object from, String getMethodName) {
        Method getMethod;
        try {
            getMethod = from.getClass().getMethod(getMethodName);
        } catch (NoSuchMethodException ignored) {
            return null;
        }
        try {
            return getMethod.invoke(from);
        } catch (Exception e) {
            return null;
        }
    }
}
