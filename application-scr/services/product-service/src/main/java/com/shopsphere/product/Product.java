package com.shopsphere.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name="products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank private String name;
    private String description;
    @NotNull @DecimalMin("0.01") private BigDecimal price;
    @NotNull @Min(0) private Integer inventory;
    @NotBlank private String category;

    public Product() {}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getName(){return name;} public void setName(String v){this.name=v;}
    public String getDescription(){return description;} public void setDescription(String v){this.description=v;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){this.price=v;}
    public Integer getInventory(){return inventory;} public void setInventory(Integer v){this.inventory=v;}
    public String getCategory(){return category;} public void setCategory(String v){this.category=v;}
}
