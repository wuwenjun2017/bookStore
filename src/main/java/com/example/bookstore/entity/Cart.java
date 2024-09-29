package com.example.bookstore.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Long userId;
    @Column(nullable = false)
    public Long bookId;
    public String title;
    public BigDecimal price;
    @Column(columnDefinition = "INT DEFAULT 0")
    public Integer quantity;

    public Cart() {

    }
    public Cart(Long id, Long userId, Long bookId, String title, BigDecimal price, Integer quantity) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

}
