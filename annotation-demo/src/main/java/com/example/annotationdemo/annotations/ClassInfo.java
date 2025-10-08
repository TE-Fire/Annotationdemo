package com.example.annotationdemo.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * 类信息注解 --用于标记类的元数据
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClassInfo {
    String author() default "unknown";      // 作者
    String version() default "1.0";         // 版本
    String description() default "";        // 描述
    String created() default "";            // 创建时间
    String[] tags() default {};             // 标签数组
}
