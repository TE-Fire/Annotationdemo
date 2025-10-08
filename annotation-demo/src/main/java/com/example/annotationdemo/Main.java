package com.example.annotationdemo;

import java.util.List;

import com.example.annotationdemo.models.Product;
import com.example.annotationdemo.models.User;
import com.example.annotationdemo.processor.AnnotationProcessor;
import com.example.annotationdemo.processor.JsonSerializer;
import com.example.annotationdemo.processor.ValidationProcessor;

public class Main {
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        
        System.out.println("🚀 注解实战项目启动");
        System.out.println("=" .repeat(50));

        try {
            //1. 创建注解处理器
            AnnotationProcessor annotationProcessor = new AnnotationProcessor();

            //2. 扫描并处理注解
            annotationProcessor.scanAndProcess("com.example.annotationdemo.models");

            //3. 创建验证处理器
            ValidationProcessor validationProcessor = new ValidationProcessor();

            //4. 测试数据验证
            testValidation(validationProcessor);

            //5. 测试JSON序列化
            testJsonSerialization();

            // 6. 测试组件创建
            testComponentCreation(annotationProcessor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     /**
     * 测试数据验证
     */
    private static void testValidation(ValidationProcessor validator) throws Exception {
        System.out.println("\n" + "=" .repeat(50));
        System.out.println("🧪 测试数据验证");
        System.out.println("=" .repeat(50));

        //创建测试用户

        User user = new User();
        user.setUserId(null);  // 违反NOT_NULL规则
        user.setUsername("ab"); // 违反LENGTH规则（最小3位）
        user.setEmail("invalid-email"); // 违反EMAIL规则
        user.setAge(-5); // 违反MIN规则
        user.setPhone("1234567890"); // 违反PHONE规则
        
        System.out.println("测试用户: " + user);

        // 执行验证
        List<String> errors = validator.validate(user);
        
        if (errors.isEmpty()) {
            System.out.println("✅ 验证通过!");
        } else {
            System.out.println("❌ 验证失败，错误信息:");
            for (String error : errors) {
                System.out.println("   - " + error);
            }
        }

        // 测试有效数据
        User validUser = new User(1L, "zhangsan", "zhangsan@example.com", 25, "13812345678");
        System.out.println("\n有效用户: " + validUser);
        errors = validator.validate(validUser);
        if (errors.isEmpty()) {
            System.out.println("✅ 有效用户验证通过!");
        }
    }
    
    /*
     * 测试JSON序列化
     */
    private static void testJsonSerialization() throws Exception {
        System.out.println("\n" + "=" .repeat(50));
        System.out.println("📄 测试JSON序列化");
        System.out.println("=" .repeat(50));

        JsonSerializer serializer = new JsonSerializer();

        //测试用户序列化
        User user = new User(1L, "zhangsan", "zhangsan@example.com", 25, "13812345678");
        String userJson = serializer.toJson(user);
        System.out.println("用户JSON: " + userJson);
        
        // 测试产品序列化
        Product product = new Product(1001L, "笔记本电脑", 5999.99, 50);
        String productJson = serializer.toJson(product);
        System.out.println("产品JSON: " + productJson);
    }

    /**
     * 测试组件创建
     */
    private static void testComponentCreation(AnnotationProcessor processor) throws Exception {
        System.out.println("\n" + "=" .repeat(50));
        System.out.println("🏭 测试组件创建");
        System.out.println("=" .repeat(50));
        
        // 创建User组件实例
        Object userComponent = processor.createInstance(User.class);
        if (userComponent != null) {
            System.out.println("✅ 成功创建User组件: " + userComponent.getClass().getSimpleName());
        }
        
        // 创建Product组件实例
        Object productComponent = processor.createInstance(Product.class);
        if (productComponent != null) {
            System.out.println("✅ 成功创建Product组件: " + productComponent.getClass().getSimpleName());
        }
    }
}
