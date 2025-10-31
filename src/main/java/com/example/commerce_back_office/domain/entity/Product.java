package com.example.commerce_back_office.domain.entity;

import com.example.commerce_back_office.domain.Category;
import com.example.commerce_back_office.domain.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor
public class Product extends BaseEntity{
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer price;

    private Integer stock;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Enumerated(EnumType.STRING)
    Category category;

    private String description;

    public Product(String name, Integer price, Integer stock, ProductStatus status, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.description = description;
    }
}
