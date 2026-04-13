package com.bookshop.service.book.impl;

import com.bookshop.common.enums.book.BookErrorCode;
import com.bookshop.exception.BusinessException;
import com.bookshop.dto.book.BookCreateDTO;
import com.bookshop.dto.book.BookUpdateDTO;
import com.bookshop.entity.book.Book;
import com.bookshop.mapper.book.BookMapper;
import com.bookshop.service.book.BookService;
import com.bookshop.vo.book.BookVO;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 图书业务实现类。
 * 作用：
 * 1) 处理 DTO 与 Entity/VO 的转换；
 * 2) 组织业务流程并调用 mapper 完成数据操作；
 * 3) 抛出统一业务异常，交给全局异常处理。
 */
@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;

    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public List<BookVO> listBooks() {
        return bookMapper.selectAll().stream().map(this::toVO).toList();
    }

    @Override
    public BookVO getBookById(Long id) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new BusinessException(BookErrorCode.BOOK_NOT_FOUND.getCode(), BookErrorCode.BOOK_NOT_FOUND.getMessage());
        }
        return toVO(book);
    }

    @Override
    public BookVO createBook(BookCreateDTO createDTO) {
        Book book = new Book();
        book.setName(createDTO.getName());
        book.setAuthor(createDTO.getAuthor());
        book.setPrice(createDTO.getPrice());
        book.setStock(createDTO.getStock());
        bookMapper.insert(book);
        return toVO(bookMapper.selectById(book.getId()));
    }

    @Override
    public BookVO updateBook(BookUpdateDTO updateDTO) {
        Book book = new Book();
        book.setId(updateDTO.getId());
        book.setName(updateDTO.getName());
        book.setAuthor(updateDTO.getAuthor());
        book.setPrice(updateDTO.getPrice());
        book.setStock(updateDTO.getStock());
        int rows = bookMapper.updateById(book);
        if (rows == 0) {
            throw new BusinessException(BookErrorCode.BOOK_NOT_FOUND.getCode(), BookErrorCode.BOOK_NOT_FOUND.getMessage());
        }
        return toVO(bookMapper.selectById(updateDTO.getId()));
    }

    @Override
    public void deleteBook(Long id) {
        int rows = bookMapper.deleteById(id);
        if (rows == 0) {
            throw new BusinessException(BookErrorCode.BOOK_DELETE_FAILED.getCode(), BookErrorCode.BOOK_DELETE_FAILED.getMessage());
        }
    }

    private BookVO toVO(Book book) {
        BookVO vo = new BookVO();
        vo.setId(book.getId());
        vo.setName(book.getName());
        vo.setAuthor(book.getAuthor());
        vo.setPrice(book.getPrice());
        vo.setStock(book.getStock());
        return vo;
    }
}
