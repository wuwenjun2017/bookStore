package com.example.bookstore.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    public Long id;
    @Column(nullable = false, unique = true)
    public String title;
    public String author;
    @Column(columnDefinition = "DECIMAL DEFAULT 0")
    public BigDecimal price;
    public String category;

    public Book() {

    }
    public Book(Long id, String title, String category, BigDecimal price, String author) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.price = price;
        this.author = author;
    }


}
