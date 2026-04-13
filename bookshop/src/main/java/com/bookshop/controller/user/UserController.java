package com.bookshop.controller.user;

import com.bookshop.common.ApiResponse;
import com.bookshop.dto.user.UserCreateDTO;
import com.bookshop.dto.user.UserUpdateDTO;
import com.bookshop.service.user.UserService;
import com.bookshop.vo.user.UserVO;
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
 * 用户模块接口层。
 * 作用：提供 user 模块的最小 CRUD 接口，遵循统一返回结构。
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<List<UserVO>> list() {
        return ApiResponse.ok("查询成功", userService.listUsers());
    }

    @GetMapping("/{id}")
    public ApiResponse<UserVO> getById(@PathVariable Long id) {
        return ApiResponse.ok("查询成功", userService.getUserById(id));
    }

    @PostMapping
    public ApiResponse<UserVO> create(@Valid @RequestBody UserCreateDTO createDTO) {
        return ApiResponse.ok("新增成功", userService.createUser(createDTO));
    }

    @PutMapping
    public ApiResponse<UserVO> update(@Valid @RequestBody UserUpdateDTO updateDTO) {
        return ApiResponse.ok("更新成功", userService.updateUser(updateDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.ok("删除成功");
    }
}
