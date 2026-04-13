package com.bookshop.service.user;

import com.bookshop.dto.user.UserCreateDTO;
import com.bookshop.dto.user.UserUpdateDTO;
import com.bookshop.vo.user.UserVO;
import java.util.List;

/**
 * 用户业务接口。
 * 作用：定义 user 模块对 controller 暴露的业务能力。
 */
public interface UserService {

    List<UserVO> listUsers();

    UserVO getUserById(Long id);

    UserVO createUser(UserCreateDTO createDTO);

    UserVO updateUser(UserUpdateDTO updateDTO);

    void deleteUser(Long id);
}
