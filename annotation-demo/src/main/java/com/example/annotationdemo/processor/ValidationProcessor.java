package com.example.annotationdemo.processor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.example.annotationdemo.annotations.Validate;

/**
 * 验证处理器 - 基于注解进行数据验证
 */
public class ValidationProcessor {
    /*
     * 验证对象
     */
    public List<String> validate(Object obj) throws IllegalAccessException {
        List<String> errors = new ArrayList<>();
        Class<?> clazz = obj.getClass();

        System.out.println("\n🔍 验证对象: " + clazz.getSimpleName());
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);

            //获取字段上的所有validate注解
            Validate[] validations = field.getAnnotationsByType(Validate.class);
            for (Validate validate : validations) {
                String error = validateField(field.getName(), value, validate);
                if (error != null) {
                    errors.add(error);
                }
            }
        }
        return errors;
    }

    private String validateField(String fieldName, Object value, Validate validate) {
        switch (validate.type()) {
            case NOT_NULL:
                if (value == null) {
                    return getErrorMessage(fieldName, validate.message(), "不能为空");
                }
                break;
            case NOT_EMPTY:
                if (value == null || value.toString().trim().isEmpty()) {
                    return getErrorMessage(fieldName, validate.message(), "不能为空");
                }
                break;
            case EMAIL:
                if (value != null && !value.toString().isEmpty()) {
                    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
                    if (!Pattern.matches(emailRegex, value.toString())) {
                        return getErrorMessage(fieldName, validate.message(), "游戏格式不正确");
                    }
                }
                break;
            case PHONE:
                if (value != null && !value.toString().isEmpty()) {
                    String phoneRegex = "^1[3-9]\\d{9}$";
                    if (!Pattern.matches(phoneRegex, value.toString())) {
                        return getErrorMessage(fieldName, validate.message(), "手机号格式不正确");
                    }
                }
                break;
            case MIN:
                if (value instanceof Number) {
                    double num = ((Number) value).doubleValue();
                    if (num < validate.min()) {
                        return getErrorMessage(fieldName, validate.message(), "不能小于" + validate.min());
                    }
                }
                break;
            case MAX:
                if (value instanceof Number) {
                    double num = ((Number) value).doubleValue();
                    if (num < validate.min()) {
                        return getErrorMessage(fieldName, validate.message(), "不能大于" + validate.max());
                    }
                }
                break;
            case LENGTH:
                if (value != null) {
                    int length = value.toString().length();
                    if (length < validate.min() || length > validate.max()) {
                        return getErrorMessage(fieldName, validate.message(), "长度必须在" + validate.min() + "-" + validate.max() + "之间");
                    }
                }
                break;
            case PATTERN:
                if (value != null && !value.toString().isEmpty() && !validate.pattern().isEmpty()) {
                    if (!Pattern.matches(validate.pattern(), value.toString())) {
                        return getErrorMessage(fieldName, validate.message(), "格式不正确");   
                    }
                }
                break;
            default:
                break;
        }
        return null;
    }

    private String getErrorMessage(String fieldName, String customMessage, String defaultMessage) {
        if (!customMessage.isEmpty()) {
            return fieldName + ": " + customMessage;
        }
        return fieldName + ": " + defaultMessage;
    }
}
