package com.example.bookstore.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartDetail{
    public Long id;
    public Long bookId;
    public Integer quantity;
    public BigDecimal price;

    public CartDetail(Long id, Long bookId, Integer quantity, BigDecimal price) {
        this.id = id;
        this.bookId = bookId;
        this.quantity = quantity;
        this.price = price;
    }
}
