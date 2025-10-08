package com.example.annotationdemo.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * 验证注解 --用于字段验证
 */

@Target(ElementType.FIELD)
@Repeatable(Validates.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
    // 验证类型枚举
    enum Type {
        NOT_NULL,       // 非空
        NOT_EMPTY,      // 非空字符串
        EMAIL,          // 邮箱格式
        PHONE,          // 手机号
        MIN,            // 最小值
        MAX,            // 最大值
        LENGTH,         // 长度范围
        PATTERN         // 正则表达式
    }
    
    Type type();                        // 验证类型
    String message() default "";        // 错误消息
    int min() default Integer.MIN_VALUE; // 最小值
    int max() default Integer.MAX_VALUE; // 最大值
    String pattern() default "";        // 正则模式
}



