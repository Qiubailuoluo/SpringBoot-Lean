package com.bookshop.mapper.book;

import com.bookshop.entity.book.Book;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 图书数据访问层。
 * 作用：封装对表 book_info 的 SQL 操作。
 * 说明：当前示例使用注解 SQL，后续复杂查询可迁移到 XML。
 */
@Mapper
public interface BookMapper {

    @Select("SELECT id, name, author, price, stock, created_at, updated_at FROM book_info ORDER BY id DESC")
    List<Book> selectAll();

    @Select("SELECT id, name, author, price, stock, created_at, updated_at FROM book_info WHERE id = #{id}")
    Book selectById(Long id);

    @Insert("""
            INSERT INTO book_info(name, author, price, stock, created_at, updated_at)
            VALUES(#{name}, #{author}, #{price}, #{stock}, NOW(), NOW())
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Book book);

    @Update("""
            UPDATE book_info
            SET name = #{name},
                author = #{author},
                price = #{price},
                stock = #{stock},
                updated_at = NOW()
            WHERE id = #{id}
            """)
    int updateById(Book book);

    @Delete("DELETE FROM book_info WHERE id = #{id}")
    int deleteById(Long id);
}
