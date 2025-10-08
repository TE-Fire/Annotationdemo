package com.example.annotationdemo.models;


import com.example.annotationdemo.annotations.ClassInfo;
import com.example.annotationdemo.annotations.Component;
import com.example.annotationdemo.annotations.FieldInfo;
import com.example.annotationdemo.annotations.JsonField;
import com.example.annotationdemo.annotations.MethodInfo;
import com.example.annotationdemo.annotations.ParamInfo;
import com.example.annotationdemo.annotations.Validate;


/*
 * 产品实体类
 */
@ClassInfo(
    author = "李四",
    version = "1.0",
    description = "产品信息实体类",
    tags = {"商品", "电商"}
)
@Component("productService")
public class Product {
    @FieldInfo(name = "产品ID", required = true)
    @Validate(type = Validate.Type.NOT_NULL, message = "产品ID不能为空")
    @JsonField(name = "productId", order = 1)
    private Long id;

    @FieldInfo(name = "产品ID", required = true)
    @Validate(type = Validate.Type.NOT_EMPTY, message = "产品名称不能为空")
    @JsonField(name = "name", order = 2)
    private String name;

    @FieldInfo(name = "价格")
    @Validate(type = Validate.Type.MIN, min = 0, message = "价格不能为负数")
    @JsonField(name = "price", order = 3)
    private Double price;

    @FieldInfo(name = "库存")
    @Validate(type = Validate.Type.MIN, min = 0, message = "库存不能为负数")
    @JsonField(name = "stock", order = 4)
    private Integer stock;

     // 构造方法
    public Product() {}
    
    public Product(Long id, String name, Double price, Integer stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
    
    // ========== 简单的Getter/Setter - 不添加注解 ==========
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    // ========== 业务方法 - 添加注解 ==========
    @MethodInfo(
        description = "检查产品是否有库存",
        returnType = "boolean"
    )
    public boolean isInSock() {
        return stock != null && stock > 0;
    }

    @MethodInfo(
        description = "减少库存量",
        returnType = "boolean"
    )
    public boolean reduceStock(
        @ParamInfo(name = "quantity", description = "减少的数量", required = true) Integer quantity) {
            if (quantity == null || quantity <= 0) {
                System.out.println("减少数量必须大于0");
                return false;
            }

            if (stock == null || stock < quantity) {
                System.out.println("库存不足，当前库存: " + stock);
                return false;
            }

            stock -= quantity;
            System.out.println("库存减少成功，库存剩余: " + stock);
            return true;
        }

     @MethodInfo(
        description = "计算折扣价格",
        returnType = "Double"
    )
    public Double calculateDiscountPrice(
        @ParamInfo(name = "discountRate", description = "折扣率", defaultValue = "0.9") Double discountRate) {
            if (price == null) return 0.0;
            if (discountRate == null) discountRate = 0.9;

            return price * discountRate;
        }
        
    @Override
    public String toString() {
        return String.format("Product{id=%d, name='%s', price=%.2f, stock=%d}", 
                        id, name, price, stock);
    }


}

