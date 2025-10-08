package com.example.annotationdemo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * 参数信息注解 --用于标记参数的元数据
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamInfo {
    String name() default "";           // 参数名称
    String description() default "";    // 参数描述
    boolean required() default true;    // 是否必须
    String defaultValue() default "";   // 默认值
}