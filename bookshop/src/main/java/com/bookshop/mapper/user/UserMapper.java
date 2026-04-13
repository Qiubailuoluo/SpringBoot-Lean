package com.bookshop.mapper.user;

import com.bookshop.entity.user.User;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用户数据访问层。
 * 作用：封装 user_account 的常用 SQL。
 */
@Mapper
public interface UserMapper {

    @Select("SELECT id, username, display_name, status, created_at, updated_at FROM user_account ORDER BY id DESC")
    List<User> selectAll();

    @Select("SELECT id, username, display_name, status, created_at, updated_at FROM user_account WHERE id = #{id}")
    User selectById(Long id);

    @Select("SELECT id, username, display_name, status, created_at, updated_at FROM user_account WHERE username = #{username}")
    User selectByUsername(String username);

    @Insert("""
            INSERT INTO user_account(username, display_name, status, created_at, updated_at)
            VALUES(#{username}, #{displayName}, #{status}, NOW(), NOW())
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("""
            UPDATE user_account
            SET display_name = #{displayName},
                status = #{status},
                updated_at = NOW()
            WHERE id = #{id}
            """)
    int updateById(User user);

    @Delete("DELETE FROM user_account WHERE id = #{id}")
    int deleteById(Long id);
}
