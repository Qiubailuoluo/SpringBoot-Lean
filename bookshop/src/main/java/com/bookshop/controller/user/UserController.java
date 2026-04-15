package com.bookshop.controller.user;

import com.bookshop.common.response.ApiResponse;
import com.bookshop.dto.user.UserCreateDTO;
import com.bookshop.dto.user.UserUpdateDTO;
import com.bookshop.service.user.UserService;
import com.bookshop.vo.user.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@Tag(name = "User", description = "用户管理接口")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "查询用户列表")
    public ApiResponse<List<UserVO>> list() {
        return ApiResponse.ok("查询成功", userService.listUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "按 ID 查询用户")
    public ApiResponse<UserVO> getById(@PathVariable Long id) {
        return ApiResponse.ok("查询成功", userService.getUserById(id));
    }

    @PostMapping
    @Operation(summary = "注册用户（开放接口）")
    public ApiResponse<UserVO> create(@Valid @RequestBody UserCreateDTO createDTO, HttpServletRequest request) {
        return ApiResponse.ok("注册成功", userService.createUser(createDTO, getClientIp(request)));
    }

    @PutMapping
    @Operation(summary = "更新用户信息")
    public ApiResponse<UserVO> update(@Valid @RequestBody UserUpdateDTO updateDTO) {
        return ApiResponse.ok("更新成功", userService.updateUser(updateDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.ok("删除成功");
    }

    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
