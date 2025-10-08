package com.example.annotationdemo.annotations;

import java.lang.annotation.*;

/**
 * JSON字段注解 - 控制JSON序列化行为
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonField {
    String name() default "";           // JSON字段名（为空使用字段名）
    boolean ignore() default false;     // 是否忽略该字段
    String format() default "";         // 格式化（日期等）
    int order() default 0;              // 序列化顺序
}
