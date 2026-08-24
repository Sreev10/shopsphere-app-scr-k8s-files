package com.shopsphere.order;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="order_id",nullable=false) private Long orderId;
    @Column(name="product_id",nullable=false) private Long productId;
    @Column(nullable=false) private Integer quantity;
    @Column(nullable=false,precision=12,scale=2) private BigDecimal unitPrice;
    public OrderItem() {}
    public Long getId(){return id;} public Long getOrderId(){return orderId;} public void setOrderId(Long v){orderId=v;}
    public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public BigDecimal getUnitPrice(){return unitPrice;} public void setUnitPrice(BigDecimal v){unitPrice=v;}
}
