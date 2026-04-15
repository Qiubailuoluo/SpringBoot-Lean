package com.bookshop.controller.book;

import com.bookshop.common.response.ApiResponse;
import com.bookshop.dto.book.BookCreateDTO;
import com.bookshop.dto.book.BookUpdateDTO;
import com.bookshop.service.book.BookService;
import com.bookshop.vo.book.BookVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图书模块接口层（book 子模块）。
 * 作用：提供图书 CRUD 的 HTTP 接口，并统一返回 ApiResponse。
 */
@RestController
@RequestMapping("/api/books")
@Tag(name = "Book", description = "图书管理接口")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "查询图书列表")
    public ApiResponse<List<BookVO>> list() {
        return ApiResponse.ok("查询成功", bookService.listBooks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "按 ID 查询图书")
    public ApiResponse<BookVO> getById(@PathVariable Long id) {
        return ApiResponse.ok("查询成功", bookService.getBookById(id));
    }

    @PostMapping
    @Operation(summary = "新增图书")
    public ApiResponse<BookVO> create(@Valid @RequestBody BookCreateDTO createDTO) {
        return ApiResponse.ok("新增成功", bookService.createBook(createDTO));
    }

    @PutMapping
    @Operation(summary = "更新图书")
    public ApiResponse<BookVO> update(@Valid @RequestBody BookUpdateDTO updateDTO) {
        return ApiResponse.ok("更新成功", bookService.updateBook(updateDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除图书")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ApiResponse.ok("删除成功");
    }
}
