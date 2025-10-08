package com.example.annotationdemo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * 方法信息注解 --用于标记方法的元数据
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodInfo {
    String description() default "";        // 方法描述
    String returnType() default "void";     // 返回类型描述
    boolean deprecated() default false;     // 是否已弃用
    String since() default "1.0";           // 从哪个版本开始
    String[] params() default {};           // 参数描述
}
