package com.example.annotationdemo.annotations;

import java.lang.annotation.*;

/**
 * 组件注解 - 模拟Spring的组件扫描
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    String value() default "";  // 组件名称
    Scope scope() default Scope.SINGLETON; // 作用域
    
    enum Scope {
        SINGLETON,  // 单例
        PROTOTYPE   // 原型
    }
}