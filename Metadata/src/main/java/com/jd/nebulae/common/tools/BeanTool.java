package com.jd.nebulae.common.tools;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.jd.nebulae.common.annotation.Column;
import com.jd.nebulae.common.annotation.Table;
import com.jd.nebulae.common.entity.DataType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
public class BeanTool {

    private static final String SPLITTER = "#^";
    private static ThreadLocal<DateFormat> sdf = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };

    //TODO 分隔符
    public static String generateId(String... args) {
        return Joiner.on(SPLITTER).skipNulls().join(args);
    }

    public static String getSplitter() {
        return SPLITTER;
    }

    public static <T> String generateUpdateSql(T t, String tableName) {
        List<String> updates = new ArrayList<>();
        Class<?> clz = t.getClass();
        String condition = null;
        Field[] fields = clz.getDeclaredFields();
        if (null == fields || fields.length == 0) {
            return null;
        }
        for (Field field : fields) {
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            String fieldName = field.getName();
            boolean isPk = false;
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                if (column.isTransient()) {
                    continue;
                } else {
                    if (!Strings.isNullOrEmpty(column.value())) {
                        fieldName = column.value();
                    }
                }
                isPk = column.isPrimaryKey();
            }
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(true);
            }
            Object value;
            try {
                value = field.get(t);
            } catch (IllegalAccessException e) {
                throw Throwables.propagate(e);
            }
            if (isPk) {
                if (value == null) {
                    throw new NullPointerException("primaryKey is null");
                } else {
                    if (value instanceof Number) {
                        condition = String.format(" where %s = %s ", fieldName, value);
                    } else {
                        condition = String.format(" where %s = '%s' ", fieldName, value);
                    }
                }
                continue;
            }
            if (value != null) {
                if (value instanceof Boolean) {
                    updates.add(String.format(" %s = %s ", fieldName, (boolean) value ? "1" : "0"));
                } else if (value instanceof Number) {
                    updates.add(String.format(" %s = %s ", fieldName, String.valueOf(value)));
                } else if (value instanceof Date) {
                    updates.add(String.format(" %s = %s ", fieldName, sdf.get().format(value)));
                } else if (value instanceof DataType) {
                    updates.add(String.format(" %s = %s ", fieldName, ((DataType) value).getValue()));
                } else if (value instanceof String) {
                    updates.add(String.format(" %s = '%s' ", fieldName, value));
                } else {
                    updates.add(String.format(" %s = '%s' ", fieldName, JSON.toJSONString(value)));
                }
            }
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(false);
            }
        }
        return String.format("update %s set %s %s", tableName, Joiner.on(",").join(updates), condition);
    }

    public static <T> String generateUpdateSql(T t) {
        if (t.getClass().isAnnotationPresent(Table.class)) {
            Table table = t.getClass().getAnnotation(Table.class);
            if (!Strings.isNullOrEmpty(table.value())) {
                return generateUpdateSql(t, table.value());
            }
        }
        return generateUpdateSql(t, t.getClass().getSimpleName());
    }

    /**
     * insert into table () values (),(),()
     */
    public static <T> String generateInsertSql(List<T> list, String tableName) {
        List<String> values = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        Set<Object> cache = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            Class<?> clz = t.getClass();
            List<String> row = new ArrayList<>();
            Field[] fields = clz.getDeclaredFields();
            if (null == fields || fields.length == 0) {
                return null;
            }
            for (Field field : fields) {
                if (Modifier.isTransient(field.getModifiers())) {
                    continue;
                }
                String fieldName = field.getName();
                boolean isPk = false;
                boolean autoCurrentTimestamp = false;
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    if (column.isTransient()) {
                        continue;
                    } else {
                        if (!Strings.isNullOrEmpty(column.value())) {
                            fieldName = column.value();
                        }
                    }
                    isPk = column.isPrimaryKey();
                    autoCurrentTimestamp = column.autoCurrentTimestamp();
                }
                if (i == 0) {
                    columns.add(fieldName);
                }
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                Object value;
                try {
                    value = field.get(t);
                } catch (IllegalAccessException e) {
                    throw Throwables.propagate(e);
                }
                if (isPk && value != null) {
                    Conditions.checkArguments(cache.add(value),
                            new IllegalArgumentException("has duplicate primaryKey " + fieldName + ", value: " + value));
                }
                if (value == null) {
                    if (autoCurrentTimestamp) {
                        row.add("CURRENT_TIMESTAMP");
                    } else {
                        row.add("null");
                    }
                } else if (value instanceof Boolean) {
                    row.add((boolean) value ? "1" : "0");
                } else if (value instanceof Number) {
                    row.add(String.valueOf(value));
                } else if (value instanceof Date) {
                    row.add(sdf.get().format(value));
                } else if (value instanceof DataType) {
                    row.add(((DataType) value).getValue());
                } else if (value instanceof String) {
                    row.add(String.format("'%s'", value));
                } else {
                    row.add(String.format("'%s'", JSON.toJSONString(value)));
                }
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(false);
                }
            }
            values.add(String.format("(%s)", Joiner.on(",").join(row)));
        }
        return String.format("insert into %s (%s) values %s;", tableName, Joiner.on(",").join(columns), Joiner.on(",").join(values));
    }

    public static <T> String generateInsertSql(List<T> list) {
        if (list.get(0).getClass().isAnnotationPresent(Table.class)) {
            Table table = list.get(0).getClass().getAnnotation(Table.class);
            if (!Strings.isNullOrEmpty(table.value())) {
                return generateInsertSql(list, table.value());
            }
        }
        return generateInsertSql(list, list.get(0).getClass().getSimpleName());
    }

    @SuppressWarnings("unchecked")
    public static <T> T mergeTo(T from, T to) {
        for (Field field : to.getClass().getDeclaredFields()) {
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(true);
            }
            Object v;
            try {
                v = field.get(from);
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    if (column.isPrimaryKey()) {
                        Conditions.checkArguments(field.get(from).equals(field.get(to)),
                                new IllegalArgumentException("can't merge different object"));
                    }
                }
                if (v == null) {
                    continue;
                }
                if (v instanceof Collection) {
                    ((Collection) field.get(from)).addAll((Collection) field.get(to));
                }
                field.set(to, v);
            } catch (IllegalAccessException e) {
                throw Throwables.propagate(e);
            }
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(false);
            }
        }
        return to;
    }

    //TODO 分隔符
    public static String generateParentId(String id) {
        int index = id.lastIndexOf(SPLITTER);
        if (index == -1) {
            return "";
        } else {
            return id.substring(0, index);
        }
    }

    public static List<String> getKeyList(Class<?> clazz) {
        List<String> keyList = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(true);
            }
            String fieldName;
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                fieldName = column.value();
            } else {
                fieldName = field.getName();
            }
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(false);
            }
            keyList.add(fieldName);
        }
        return keyList;
    }
}
