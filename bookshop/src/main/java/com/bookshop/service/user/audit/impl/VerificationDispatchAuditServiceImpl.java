package com.bookshop.service.user.audit.impl;

import com.bookshop.entity.user.VerificationDispatchAudit;
import com.bookshop.mapper.user.VerificationDispatchAuditMapper;
import com.bookshop.service.user.audit.VerificationDispatchAuditService;
import com.bookshop.service.user.verification.VerificationDispatchResult;
import com.bookshop.vo.user.VerificationDispatchAuditVO;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 验证码发送回执审计服务实现。
 */
@Service
public class VerificationDispatchAuditServiceImpl implements VerificationDispatchAuditService {

    private static final Logger log = LoggerFactory.getLogger(VerificationDispatchAuditServiceImpl.class);

    private final VerificationDispatchAuditMapper auditMapper;

    public VerificationDispatchAuditServiceImpl(VerificationDispatchAuditMapper auditMapper) {
        this.auditMapper = auditMapper;
    }

    @Override
    public void recordSuccess(String target, VerificationDispatchResult result) {
        VerificationDispatchAudit audit = new VerificationDispatchAudit();
        audit.setTarget(target);
        audit.setDeliveryId(result.getDeliveryId());
        audit.setChannel(result.getChannel());
        audit.setCodeMasked(maskCode(result.getCode()));
        audit.setIsMock(result.isMock());
        audit.setSuccess(true);
        auditMapper.insert(audit);
    }

    @Override
    public void recordFailure(String target, String errorMessage) {
        VerificationDispatchAudit audit = new VerificationDispatchAudit();
        audit.setTarget(target);
        audit.setSuccess(false);
        audit.setIsMock(true);
        audit.setErrorMessage(truncate(errorMessage, 255));
        auditMapper.insert(audit);
    }

    @Override
    public List<VerificationDispatchAuditVO> listRecent(
            int limit, String target, Boolean success, LocalDateTime fromTime, LocalDateTime toTime) {
        int safeLimit = Math.max(1, Math.min(limit, 100));
        return auditMapper.selectRecent(safeLimit, target, success, fromTime, toTime).stream()
                .map(this::toVO)
                .toList();
    }

    private String maskCode(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        if (code.length() <= 2) {
            return "**";
        }
        return "****" + code.substring(code.length() - 2);
    }

    private String truncate(String raw, int max) {
        if (raw == null) {
            return null;
        }
        if (raw.length() <= max) {
            return raw;
        }
        log.warn("审计错误信息过长，已截断");
        return raw.substring(0, max);
    }

    private VerificationDispatchAuditVO toVO(VerificationDispatchAudit audit) {
        VerificationDispatchAuditVO vo = new VerificationDispatchAuditVO();
        vo.setId(audit.getId());
        vo.setTarget(audit.getTarget());
        vo.setDeliveryId(audit.getDeliveryId());
        vo.setChannel(audit.getChannel());
        vo.setCodeMasked(audit.getCodeMasked());
        vo.setIsMock(audit.getIsMock());
        vo.setSuccess(audit.getSuccess());
        vo.setErrorMessage(audit.getErrorMessage());
        vo.setCreatedAt(audit.getCreatedAt());
        return vo;
    }
}
