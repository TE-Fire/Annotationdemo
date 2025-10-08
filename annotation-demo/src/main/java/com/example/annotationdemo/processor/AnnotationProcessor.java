package com.example.annotationdemo.processor;

import java.util.Arrays;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.example.annotationdemo.annotations.ClassInfo;
import com.example.annotationdemo.annotations.Component;
import com.example.annotationdemo.annotations.FieldInfo;
import com.example.annotationdemo.annotations.JsonField;
import com.example.annotationdemo.annotations.MethodInfo;
import com.example.annotationdemo.annotations.ParamInfo;
import com.example.annotationdemo.annotations.Validate;

/*
* 注解处理器 - 演示如何通过反射读取各种注解
*/

public class AnnotationProcessor {
    
    /*
     * 扫描并处理指定包下的所有类
     */
    public void scanAndProcess(String packageName) {
        System.out.println("开始扫描包：" + packageName);
        System.out.println("=".repeat(50));

        //模拟扫描到的类
        Class<?>[] classes = {com.example.annotationdemo.models.User.class,
                            com.example.annotationdemo.models.Product.class};
        
        for (Class<?> clazz : classes) {
            processClass(clazz);
        }
    }

    /*
     * 处理各级注解
     */
    private void processClass(Class<?> clazz) {
        System.out.println("\n🔍 处理类: " + clazz.getSimpleName());
        // 1. 处理类级别注解
        processClassLevelAnnotations(clazz);
        
        // 2. 处理字段级别注解
        processFieldLevelAnnotations(clazz);
        
        // 3. 处理方法级别注解
        processMethodLevelAnnotations(clazz);
        
        // 4. 处理方法参数注解
        processParameterAnnotations(clazz);
    }

    /**
     * 处理类级别注解
     */
    private void processClassLevelAnnotations(Class<?> clazz) {
        System.out.println("\n📋 类级别注解:");
        
        // 检查ClassInfo注解
        if (clazz.isAnnotationPresent(ClassInfo.class)) {
            ClassInfo classInfo = clazz.getAnnotation(ClassInfo.class);
            System.out.println("  🏷️  ClassInfo:");
            System.out.println("     作者: " + classInfo.author());
            System.out.println("     版本: " + classInfo.version());
            System.out.println("     描述: " + classInfo.description());
            System.out.println("     创建: " + classInfo.created());
            System.out.println("     标签: " + Arrays.toString(classInfo.tags()));
        }
        
        // 检查Component注解
        if (clazz.isAnnotationPresent(Component.class)) {
            Component component = clazz.getAnnotation(Component.class);
            System.out.println("  🧩 Component:");
            System.out.println("     名称: " + component.value());
            System.out.println("     作用域: " + component.scope());
        }
    }

    /**
     * 处理字段级别注解
     */
    private void processFieldLevelAnnotations(Class<?> clazz) {
        System.out.println("\n🏷️  字段级别注解:");
        
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("  📊 字段: " + field.getName() + " (" + field.getType().getSimpleName() + ")");
            
            // FieldInfo注解
            if (field.isAnnotationPresent(FieldInfo.class)) {
                FieldInfo fieldInfo = field.getAnnotation(FieldInfo.class);
                System.out.println("     📝 FieldInfo:");
                System.out.println("       显示名称: " + fieldInfo.name());
                System.out.println("       描述: " + fieldInfo.description());
                System.out.println("       必填: " + fieldInfo.required());
                System.out.println("       长度范围: " + fieldInfo.minLength() + "-" + fieldInfo.maxLength());
            }
            
            // Validate注解（可能有多个）
            Validate[] validations = field.getAnnotationsByType(Validate.class);
            if (validations.length > 0) {
                System.out.println("     ✅ Validate验证规则:");
                for (Validate validate : validations) {
                    System.out.println("       - 类型: " + validate.type() + ", 消息: " + validate.message());
                }
            }
            
            // JsonField注解
            if (field.isAnnotationPresent(JsonField.class)) {
                JsonField jsonField = field.getAnnotation(JsonField.class);
                System.out.println("     📄 JsonField:");
                System.out.println("       JSON字段名: " + (jsonField.name().isEmpty() ? field.getName() : jsonField.name()));
                System.out.println("       是否忽略: " + jsonField.ignore());
                System.out.println("       顺序: " + jsonField.order());
            }
        }
    }

     /**
     * 处理方法级别注解
     */
    private void processMethodLevelAnnotations(Class<?> clazz) {
        System.out.println("\n🛠️  方法级别注解:");
        
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            // 跳过合成方法和桥接方法
            if (method.isSynthetic()) continue;
            
            System.out.println("  ⚙️  方法: " + method.getName() + getParameterString(method));
            
            // MethodInfo注解
            if (method.isAnnotationPresent(MethodInfo.class)) {
                MethodInfo methodInfo = method.getAnnotation(MethodInfo.class);
                System.out.println("     📋 MethodInfo:");
                System.out.println("       描述: " + methodInfo.description());
                System.out.println("       返回类型: " + methodInfo.returnType());
                System.out.println("       是否弃用: " + methodInfo.deprecated());
                System.out.println("       起始版本: " + methodInfo.since());
                if (methodInfo.params().length > 0) {
                    System.out.println("       参数描述: " + Arrays.toString(methodInfo.params()));
                }
            }
        }
    }
    
    /**
     * 处理方法参数注解
     */
    private void processParameterAnnotations(Class<?> clazz) {
        System.out.println("\n🎯 方法参数注解:");
        
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Parameter[] parameters = method.getParameters();
            boolean hasParamAnnotations = false;
            
            for (Parameter param : parameters) {
                if (param.isAnnotationPresent(ParamInfo.class)) {
                    if (!hasParamAnnotations) {
                        System.out.println("  🔧 方法: " + method.getName());
                        hasParamAnnotations = true;
                    }
                    ParamInfo paramInfo = param.getAnnotation(ParamInfo.class);
                    System.out.println("     📍 参数: " + param.getName());
                    System.out.println("       名称: " + paramInfo.name());
                    System.out.println("       描述: " + paramInfo.description());
                    System.out.println("       必须: " + paramInfo.required());
                    System.out.println("       默认值: " + paramInfo.defaultValue());
                }
            }
        }
    }

    /*
     * 生成参数字符串
     */
    private String getParameterString(Method method) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length == 0) return "()";

        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(parameters[i].getType().getSimpleName())
                .append(" ")
                .append(parameters[i].getName());
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * 创建对象实例（模拟IoC容器）
     */
    public Object createInstance(Class<?> clazz) throws Exception {
        if (clazz.isAnnotationPresent(Component.class)) {
            System.out.println("\n🏭 创建组件实例: " + clazz.getSimpleName());
            return clazz.getDeclaredConstructor().newInstance();
        }
        return null;
    }
}

