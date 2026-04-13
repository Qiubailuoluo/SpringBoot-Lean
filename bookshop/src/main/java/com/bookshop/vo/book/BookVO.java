package com.bookshop.vo.book;

import java.math.BigDecimal;
import lombok.Data;

/**
 * 图书返回 VO。
 * 作用：统一对外响应结构，避免直接暴露 Entity 细节。
 */
@Data
public class BookVO {

    private Long id;
    private String name;
    private String author;
    private BigDecimal price;
    private Integer stock;

}
