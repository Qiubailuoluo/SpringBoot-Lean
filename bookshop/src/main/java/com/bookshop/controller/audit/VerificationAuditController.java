package com.bookshop.controller.audit;

import com.bookshop.common.response.ApiResponse;
import com.bookshop.service.user.audit.VerificationDispatchAuditService;
import com.bookshop.vo.user.VerificationDispatchAuditVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码发送审计查询接口。
 * 作用：提供最近回执审计记录查看能力，便于排障和联调。
 */
@RestController
@RequestMapping("/api/audit/verification")
@Tag(name = "Audit", description = "验证码回执审计接口")
public class VerificationAuditController {

    private final VerificationDispatchAuditService auditService;

    public VerificationAuditController(VerificationDispatchAuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * 查询验证码回执审计记录（默认最近 20 条，支持筛选）。
     */
    @GetMapping("/recent")
    @Operation(summary = "查询最近验证码回执审计记录")
    public ApiResponse<List<VerificationDispatchAuditVO>> recent(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String target,
            @RequestParam(required = false) Boolean success,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toTime) {
        return ApiResponse.ok("查询成功", auditService.listRecent(limit, target, success, fromTime, toTime));
    }
}
