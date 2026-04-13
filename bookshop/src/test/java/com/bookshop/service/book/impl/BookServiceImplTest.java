package com.bookshop.service.book.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bookshop.exception.BusinessException;
import com.bookshop.entity.book.Book;
import com.bookshop.mapper.book.BookMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * BookServiceImpl 单元测试。
 * 作用：验证图书业务层在核心场景下的行为，避免重构时破坏业务约束。
 */
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void getBookById_whenBookExists_shouldReturnBookVO() {
        Book book = new Book();
        book.setId(1L);
        book.setName("测试图书");
        book.setAuthor("作者A");
        book.setPrice(BigDecimal.TEN);
        book.setStock(9);
        when(bookMapper.selectById(1L)).thenReturn(book);

        var result = bookService.getBookById(1L);

        assertEquals(1L, result.getId());
        assertEquals("测试图书", result.getName());
    }

    @Test
    void getBookById_whenBookMissing_shouldThrowBusinessException() {
        when(bookMapper.selectById(99L)).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class, () -> bookService.getBookById(99L));

        assertEquals("BOOK_404", ex.getCode());
    }

    @Test
    void deleteBook_whenBookExists_shouldCallDelete() {
        when(bookMapper.deleteById(1L)).thenReturn(1);

        bookService.deleteBook(1L);

        verify(bookMapper).deleteById(1L);
    }
}
