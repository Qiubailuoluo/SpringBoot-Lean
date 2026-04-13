package com.bookshop.vo.book;

import java.math.BigDecimal;

/**
 * 图书返回 VO。
 * 作用：统一对外响应结构，避免直接暴露 Entity 细节。
 */
public class BookVO {

    private Long id;
    private String name;
    private String author;
    private BigDecimal price;
    private Integer stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
