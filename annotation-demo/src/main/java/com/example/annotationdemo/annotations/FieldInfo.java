package com.example.annotationdemo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * 字段信息注解 --用于标记字段的元数据
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldInfo {
    String name() default "";           // 字段显示名称
    String description() default "";    // 字段描述
    boolean required() default false;   // 是否必填
    int minLength() default 0;          // 最小长度
    int maxLength() default Integer.MAX_VALUE; // 最大长度
    String pattern() default "";        // 正则验证模式
}
