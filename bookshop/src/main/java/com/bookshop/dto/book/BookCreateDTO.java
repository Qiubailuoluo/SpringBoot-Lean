package com.bookshop.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 新增图书请求 DTO。
 * 作用：约束新增接口的入参格式，避免 controller 直接接收实体对象。
 */
@Data
@Schema(description = "新增图书请求参数")
public class BookCreateDTO {

    @NotBlank(message = "书名不能为空")
    @Schema(description = "书名", example = "Spring Boot 实战")
    private String name;

    @NotBlank(message = "作者不能为空")
    @Schema(description = "作者", example = "王老师")
    private String author;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.0", inclusive = false, message = "价格必须大于0")
    @Schema(description = "价格（大于0）", example = "59.90")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能小于0")
    @Schema(description = "库存（大于等于0）", example = "120")
    private Integer stock;

}
