package com.bookshop.vo.book;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 图书返回 VO。
 * 作用：统一对外响应结构，避免直接暴露 Entity 细节。
 */
@Data
@Schema(description = "图书响应结果")
public class BookVO {

    @Schema(description = "图书ID", example = "1")
    private Long id;
    @Schema(description = "书名", example = "Spring Boot 实战")
    private String name;
    @Schema(description = "作者", example = "王老师")
    private String author;
    @Schema(description = "价格", example = "59.90")
    private BigDecimal price;
    @Schema(description = "库存", example = "120")
    private Integer stock;

}
