package com.example.annotationdemo.processor;

import java.lang.reflect.Field;

import com.example.annotationdemo.annotations.JsonField;

import java.util.*;
/*
 * JSON序列化器 --基于注解控制序列化行为
 */
public class JsonSerializer {
    
    /*
     * 对象序列化为JSON字段串
     */
    public String toJson(Object obj) throws IllegalAccessException {
        if (obj == null) return "null";

        Class<?> clazz = obj.getClass();
        // StringBuilder json = new StringBuilder();

        if (isPrimitiveOrWrapper(obj)) {
            //基本类型直接返回
            return formatValue(obj);
        } else if (obj instanceof String) {
            //字符串类型
            return "\"" + escapeJson(obj.toString()) + "\"";
        } else if (obj instanceof Collection) {
            //集合类型
            return collectionToJson((Collection<?>) obj);
        } else if (obj instanceof Map) {
            return mapToJson((Map<?, ?>) obj);
        } else {
            //对象类型
            return objectToJson(obj, clazz);
        }
    }
    /*
     * 格式化值
     */
    private String formatValue(Object value) {
        if (value == null) return "null";
        if (value instanceof String) return "\"" + escapeJson(value.toString()) + "\"";
        if (value instanceof Character) return "\"" + escapeJson(value.toString()) + "\"";
        return value.toString();
    }

    /*
     * 对象转JSON
     */
    private String objectToJson(Object obj, Class<?> clazz) throws IllegalAccessException {
        StringBuilder json = new StringBuilder("{");
        List<FieldEntry> fields = getSortedFields(clazz);

        boolean first = true;
        for (FieldEntry entry : fields) {
            Field field = entry.field;
            field.setAccessible(true);
            Object value = field.get(obj);

            JsonField jsonField = field.getAnnotation(JsonField.class);
            if (jsonField != null && jsonField.ignore()) {
                continue;
            }

            if (!first) {
                json.append(",");
            }
            first = false;

            //字段名
            String fieldName = (jsonField != null && !jsonField.name().isEmpty()) ? 
                                jsonField.name() : field.getName();
            json.append("\"").append(fieldName).append("\":");

            //字段值
            json.append(toJson(value));
        }
        json.append("}");
        return json.toString();
    }

    /*
     * 获取排序后的字段列表
     */
    private List<FieldEntry> getSortedFields(Class<?> clazz) {
        List<FieldEntry> fields = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            JsonField jsonField = field.getAnnotation(JsonField.class);
            int order = (jsonField != null) ? jsonField.order() : Integer.MAX_VALUE;
            fields.add(new FieldEntry(field, order));
        }

        //按order排序
        fields.sort(Comparator.comparingInt(FieldEntry::getOrder));
        return fields;
    }

    /*
     * 集合转JSON
     */
    private String collectionToJson(Collection<?> collection) throws IllegalAccessException {
        StringBuilder json = new StringBuilder();
        boolean first = true;

        for (Object item : collection) {
            if (!first) {
                json.append(",");
            }
            first = false;
            json.append(toJson(item));
        }
        json.append("]");
        return json.toString();
    }

    /*
     * Map转Json
     */
    private String mapToJson(Map<?, ?> map) throws IllegalAccessException {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            first = false;
            json.append("\"").append(entry.getKey()).append("\":").append(toJson(entry.getValue()));
        }
        json.append("}");
        return json.toString();
    }

    /*
     * 转义JSON特殊字符串
     */
    private String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\b", "\\b")
                    .replace("\f", "\\f")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");            
    }
    /*
     * 判断是否是基本类型或包装类型
     */
    private boolean isPrimitiveOrWrapper(Object obj) {
        return obj instanceof Number ||
                obj instanceof Boolean ||
                obj instanceof Character;
    }

    /*
     * 字段条目（用于排序）
     */
    private static class FieldEntry {
        Field field;
        int order;

        FieldEntry(Field field, int order) {
            this.field = field;
            this.order = order;
        }

        int getOrder() {
            return order;
        }
    }
}
