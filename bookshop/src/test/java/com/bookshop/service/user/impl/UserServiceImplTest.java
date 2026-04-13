package com.bookshop.service.user.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bookshop.common.exception.BusinessException;
import com.bookshop.dto.user.UserCreateDTO;
import com.bookshop.entity.user.User;
import com.bookshop.mapper.user.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * UserServiceImpl 单元测试。
 * 作用：验证用户业务层的关键分支，确保错误码和约束逻辑稳定。
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_whenUsernameDuplicated_shouldThrowBusinessException() {
        User existed = new User();
        existed.setId(1L);
        existed.setUsername("qiubai");
        when(userMapper.selectByUsername("qiubai")).thenReturn(existed);

        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("qiubai");
        dto.setDisplayName("秋白");

        BusinessException ex = assertThrows(BusinessException.class, () -> userService.createUser(dto));
        assertEquals("USER_409", ex.getCode());
    }

    @Test
    void getUserById_whenUserMissing_shouldThrowBusinessException() {
        when(userMapper.selectById(99L)).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class, () -> userService.getUserById(99L));
        assertEquals("USER_404", ex.getCode());
    }

    @Test
    void deleteUser_whenUserExists_shouldCallDelete() {
        when(userMapper.deleteById(3L)).thenReturn(1);

        userService.deleteUser(3L);

        verify(userMapper).deleteById(3L);
    }
}
