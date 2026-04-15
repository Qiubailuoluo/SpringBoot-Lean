package com.bookshop.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 更新图书请求 DTO。
 * 作用：承载更新接口的入参，并由校验注解保证基础数据合法性。
 */
@Data
@Schema(description = "更新图书请求参数")
public class BookUpdateDTO {

    @NotNull(message = "图书ID不能为空")
    @Schema(description = "图书ID", example = "1")
    private Long id;

    @NotBlank(message = "书名不能为空")
    @Schema(description = "书名", example = "Spring Boot 实战（第二版）")
    private String name;

    @NotBlank(message = "作者不能为空")
    @Schema(description = "作者", example = "王老师")
    private String author;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.0", inclusive = false, message = "价格必须大于0")
    @Schema(description = "价格（大于0）", example = "69.90")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能小于0")
    @Schema(description = "库存（大于等于0）", example = "88")
    private Integer stock;

}
