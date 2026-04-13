package com.bookshop.entity.book;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 图书实体类（对应数据库表 book_info）。
 * 作用：
 * 1) 在数据访问层承载数据库字段；
 * 2) 作为 Service 内部流转对象，不直接对外暴露给接口层。
 */
@Data
public class Book {

    private Long id;
    private String name;
    private String author;
    private BigDecimal price;
    private Integer stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
