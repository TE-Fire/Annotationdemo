package com.example.annotationdemo.models;



import com.example.annotationdemo.annotations.ClassInfo;
import com.example.annotationdemo.annotations.Component;
import com.example.annotationdemo.annotations.FieldInfo;
import com.example.annotationdemo.annotations.JsonField;
import com.example.annotationdemo.annotations.MethodInfo;
import com.example.annotationdemo.annotations.ParamInfo;
import com.example.annotationdemo.annotations.Validate;

/*
 * 用户实体类 --展示各种注解的使用
 */

@ClassInfo(
    author = "张三",
    version = "1.2",
    description = "用户实体类，包含用户基本信",
    created = "2025-10-5",
    tags = {"实体类", "用户管理", "核心业务"}
)
@Component("userService") //标记为组件
public class User {
     @FieldInfo(name = "用户ID", description = "用户的唯一标识符", required = true)
    @Validate(type = Validate.Type.NOT_NULL, message = "用户ID不能为空")
    @JsonField(name = "id", order = 1)
    private Long userId;
    
    @FieldInfo(name = "用户名", description = "用户登录名称", required = true)
    @Validate(type = Validate.Type.NOT_EMPTY, message = "用户名不能为空")
    @Validate(type = Validate.Type.LENGTH, min = 3, max = 20, message = "用户名长度3-20位")
    @JsonField(name = "username", order = 2)
    private String username;
    
    @FieldInfo(name = "邮箱", description = "用户电子邮箱地址")
    @Validate(type = Validate.Type.EMAIL, message = "邮箱格式不正确")
    @JsonField(name = "email", order = 3)
    private String email;
    
    @FieldInfo(name = "年龄", description = "用户年龄")
    @Validate(type = Validate.Type.MIN, min = 0, message = "年龄不能小于0")
    @Validate(type = Validate.Type.MAX, max = 150, message = "年龄不能大于150")
    @JsonField(name = "age", order = 4)
    private Integer age;
    
    @FieldInfo(name = "手机号", description = "用户手机号码")
    @Validate(type = Validate.Type.PHONE, message = "手机号格式不正确")
    @JsonField(ignore = true)
    private String phone;
    
    // 构造方法
    public User() {}
    
    public User(Long userId, String username, String email, Integer age, String phone) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.age = age;
        this.phone = phone;
    }
    
    // ========== 简单的Getter/Setter方法 - 不添加注解 ==========
    // 这些是简单的数据访问方法，不需要额外的元数据描述
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    // ========== 业务方法 - 添加方法级别注解 ==========
    @MethodInfo(
        description = "用户注册方法，包含基本的业务逻辑验证",
        returnType = "boolean",
        params = {"用户名", "密码", "邮箱"}
    )
    public boolean register(
        @ParamInfo(name = "username", description = "用户名", required = true) String username,
        @ParamInfo(name = "password", description = "密码", required = true) String password,
        @ParamInfo(name = "email", description = "邮箱", defaultValue = "") String email) {
            //业务逻辑: 验证用户名是否已存在
            if (isUsernameExists(username)) {
                System.out.println("用户已存在：" + username );
                return false;
            }

            //业务逻辑：验证密码强度
            if (!isPasswordValid(password)) {
                System.out.println("密码强度不足");
                return false;
            }

            //模拟注册过程
            this.username = username;
            this.email = email;
            System.out.println("用户注册成功: " + username);
            return true;
        }

        @MethodInfo(
            description = "用户登录验证",
            returnType = "boolean"
        )
        public boolean login(
        @ParamInfo(name = "username", description = "用户名") String username,
        @ParamInfo(name = "password", description = "密码") String password) {
            //模拟登录验证
            boolean sucess = this.username.equals(username) && verifyPassword(password);
            System.out.println("用户登录" + (sucess ? "成功" : "失败") + username);
            return sucess;
        }

        @MethodInfo(
            description = "更新用户资料",
            returnType = "boolean"
        )
        public boolean updateProfile(
            @ParamInfo(name = "email", description = "新邮箱") String email,
            @ParamInfo(name = "age", description = "新年龄") Integer age) {
                if (email != null && !email.isEmpty()) {
                    this.email = email;
                }
                if (age != null && age >= 0) {
                    this.age = age;
                }
                System.out.println("用户资料更新成功");
                return true;
            }
        
        @MethodInfo(
            description = "检查用户是否成年",
            returnType = "boolean"
        )
        public boolean isAdult() {
            return this.age != null && this.age >= 18;
        }

        @MethodInfo(
            description = "获取用户显示名称",
            returnType = "String",
            deprecated = true,
            since = "1.0"
        )
        public String getDisplayName() {
            //已启用方法
            return "用户" + username;
        }

        @MethodInfo(
            description = "获取用户详细信息",
            returnType = "String"
        )
        public String getUserDetail() {
            return String.format("用户详情 -ID: %d, 姓名: %s, 邮箱: %s, 年龄: %d, 成年: %s", 
                    userId, username, email, age, isAdult() ? "是" : "否");
        }
    // ========== 私有方法 - 不添加注解 ==========
    
    /**
     * 检查用户名是否存在（私有方法，不添加注解）
     */
    private boolean isUsernameExists(String username) {
        // 模拟数据库查询
        return "admin".equals(username) || "test".equals(username);
    }

    /**
     * 验证密码强度（私有方法，不添加注解）
     */
    private boolean isPasswordValid(String password) {
        return password != null && password.length() >= 6;
    }

     /**
     * 验证密码（私有方法，不添加注解）
     */
    private boolean verifyPassword(String password) {
        // 模拟密码验证，实际中应该是加密验证
        return "123456".equals(password); // 简单演示
    }

    @Override
    public String toString() {
        return String.format("User{userId=%d, username='%s', email='%s', age=%d, phone='%s'}", 
                           userId, username, email, age, phone);
    }
}
