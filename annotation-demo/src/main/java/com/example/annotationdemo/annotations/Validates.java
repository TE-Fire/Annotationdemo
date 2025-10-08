package com.example.annotationdemo.annotations;

import java.lang.annotation.*;

/**
 * Validate注解的容器注解 - 用于支持重复注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validates {
    Validate[] value();  // 必须命名为value，且返回注解数组
}
