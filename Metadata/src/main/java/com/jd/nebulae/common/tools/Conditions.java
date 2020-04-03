package com.jd.nebulae.common.tools;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.jd.nebulae.common.annotation.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
public class Conditions {

    /**
     * 如果arg为String类型:空或者null返回true
     * 否则当arg为null或者arg存在NotNull注释的字段为null时返回true
     */
    public static Boolean hasNullOrEmpty(Object... args) {
        for (Object arg : args) {
            if (arg instanceof String && Strings.isNullOrEmpty(String.valueOf(arg))) {
                return true;
            }
            if (arg == null) {
                return true;
            } else {
                Field[] fields = arg.getClass().getDeclaredFields();
                for (Field field : fields) {
                    NotNull notNull = field.getAnnotation(NotNull.class);
                    try {
                        if (notNull != null) {
                            if (Modifier.isPrivate(field.getModifiers())) {
                                field.setAccessible(true);
                            }
                            if (field.get(arg) == null) {
                                if (Modifier.isPrivate(field.getModifiers())) {
                                    field.setAccessible(false);
                                }
                                return true;
                            }
                            if (Modifier.isPrivate(field.getModifiers())) {
                                field.setAccessible(false);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        Throwables.propagate(e);
                    }
                }
            }
        }
        return false;
    }

    public static List<String> getNullableField(Object object) {
        List<String> result = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            NotNull notNull = field.getAnnotation(NotNull.class);
            try {
                if (notNull != null) {
                    if (Modifier.isPrivate(field.getModifiers())) {
                        field.setAccessible(true);
                    }
                    if (field.get(object) == null) {
                        result.add(field.getName());
                    }
                    if (Modifier.isPrivate(field.getModifiers())) {
                        field.setAccessible(false);
                    }
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }
        return result;
    }

    public static void checkArguments(boolean expression, Object errorMessage) {
        Preconditions.checkArgument(expression, errorMessage);
    }
}
