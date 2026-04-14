package com.bookshop.service.login.impl;

import com.bookshop.dto.login.LoginRequestDTO;
import com.bookshop.service.login.LoginService;
import com.bookshop.vo.login.LoginResultVO;
import org.springframework.stereotype.Service;

/**
 * 登录业务实现（骨架阶段）。
 * 作用：固定返回占位说明，便于 URL 与分层结构先行稳定；真实逻辑见演进路线图 Security 章节。
 */
@Service
public class LoginServiceImpl implements LoginService {

    private static final String PLACEHOLDER_MESSAGE =
            "骨架阶段：未校验密码、未签发 JWT。下一阶段接入 Spring Security + JWT 后替换此实现。";

    @Override
    public LoginResultVO login(LoginRequestDTO requestDTO) {
        LoginResultVO vo = new LoginResultVO();
        vo.setMessage(PLACEHOLDER_MESSAGE + " 当前用户名: " + requestDTO.getUsername());
        vo.setToken(null);
        vo.setExpiresIn(null);
        return vo;
    }

    @Override
    public void logout() {
        // 骨架阶段无状态可清理；后续可在此作废 refresh token 或清理 Redis 会话。
    }
}
