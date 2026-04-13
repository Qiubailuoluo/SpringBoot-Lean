package com.bookshop.service.user.impl;

import com.bookshop.common.enums.user.UserErrorCode;
import com.bookshop.common.exception.BusinessException;
import com.bookshop.dto.user.UserCreateDTO;
import com.bookshop.dto.user.UserUpdateDTO;
import com.bookshop.entity.user.User;
import com.bookshop.mapper.user.UserMapper;
import com.bookshop.service.user.UserService;
import com.bookshop.vo.user.UserVO;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 用户业务实现类。
 * 作用：处理用户业务逻辑、对象转换和错误抛出。
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<UserVO> listUsers() {
        return userMapper.selectAll().stream().map(this::toVO).toList();
    }

    @Override
    public UserVO getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(UserErrorCode.USER_NOT_FOUND.getCode(), UserErrorCode.USER_NOT_FOUND.getMessage());
        }
        return toVO(user);
    }

    @Override
    public UserVO createUser(UserCreateDTO createDTO) {
        User existed = userMapper.selectByUsername(createDTO.getUsername());
        if (existed != null) {
            throw new BusinessException(UserErrorCode.USERNAME_DUPLICATE.getCode(), UserErrorCode.USERNAME_DUPLICATE.getMessage());
        }
        User user = new User();
        user.setUsername(createDTO.getUsername());
        user.setDisplayName(createDTO.getDisplayName());
        // 1 表示启用状态，作为初始化默认值。
        user.setStatus(1);
        userMapper.insert(user);
        return toVO(userMapper.selectById(user.getId()));
    }

    @Override
    public UserVO updateUser(UserUpdateDTO updateDTO) {
        User user = new User();
        user.setId(updateDTO.getId());
        user.setDisplayName(updateDTO.getDisplayName());
        user.setStatus(updateDTO.getStatus());
        int rows = userMapper.updateById(user);
        if (rows == 0) {
            throw new BusinessException(UserErrorCode.USER_NOT_FOUND.getCode(), UserErrorCode.USER_NOT_FOUND.getMessage());
        }
        return toVO(userMapper.selectById(updateDTO.getId()));
    }

    @Override
    public void deleteUser(Long id) {
        int rows = userMapper.deleteById(id);
        if (rows == 0) {
            throw new BusinessException(UserErrorCode.USER_DELETE_FAILED.getCode(), UserErrorCode.USER_DELETE_FAILED.getMessage());
        }
    }

    private UserVO toVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setDisplayName(user.getDisplayName());
        vo.setStatus(user.getStatus());
        return vo;
    }
}
