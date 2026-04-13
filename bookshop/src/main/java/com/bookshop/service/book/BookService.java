package com.bookshop.service.book;

import com.bookshop.dto.book.BookCreateDTO;
import com.bookshop.dto.book.BookUpdateDTO;
import com.bookshop.vo.book.BookVO;
import java.util.List;

/**
 * 图书业务层接口。
 * 作用：定义图书模块对 controller 提供的业务能力。
 */
public interface BookService {

    List<BookVO> listBooks();

    BookVO getBookById(Long id);

    BookVO createBook(BookCreateDTO createDTO);

    BookVO updateBook(BookUpdateDTO updateDTO);

    void deleteBook(Long id);
}
