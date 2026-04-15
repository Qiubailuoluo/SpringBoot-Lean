package com.bookshop.service.user.audit;

import com.bookshop.service.user.verification.VerificationDispatchResult;
import com.bookshop.vo.user.VerificationDispatchAuditVO;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 验证码发送回执审计服务。
 */
public interface VerificationDispatchAuditService {

    void recordSuccess(String target, VerificationDispatchResult result);

    void recordFailure(String target, String errorMessage);

    List<VerificationDispatchAuditVO> listRecent(
            int limit,
            String target,
            Boolean success,
            LocalDateTime fromTime,
            LocalDateTime toTime);
}
