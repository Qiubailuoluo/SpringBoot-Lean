package com.bookshop.common.exception;

import com.bookshop.common.ApiResponse;
import com.bookshop.common.trace.TraceIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器。
 * 作用：统一处理系统异常和业务异常，保证接口返回结构稳定。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常，返回业务错误码。
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        String traceId = String.valueOf(request.getAttribute(TraceIdFilter.TRACE_ID_KEY));
        log.warn("业务异常: traceId={}, code={}, message={}", traceId, ex.getCode(), ex.getMessage());
        return ApiResponse.fail(ex.getCode(), ex.getMessage(), Instant.now().toEpochMilli(), request.getRequestURI(), traceId);
    }

    /**
     * 处理参数校验异常，返回首个校验失败信息。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = "参数校验失败";
        if (ex.getBindingResult().hasFieldErrors() && ex.getBindingResult().getFieldError() != null) {
            message = ex.getBindingResult().getFieldError().getDefaultMessage();
        }
        String traceId = String.valueOf(request.getAttribute(TraceIdFilter.TRACE_ID_KEY));
        log.warn("参数校验异常: traceId={}, message={}", traceId, message);
        return ApiResponse.fail("VALIDATION_400", message, Instant.now().toEpochMilli(), request.getRequestURI(), traceId);
    }

    /**
     * 兜底处理未知异常，避免堆栈信息直接暴露给客户端。
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception ex, HttpServletRequest request) {
        String traceId = String.valueOf(request.getAttribute(TraceIdFilter.TRACE_ID_KEY));
        log.error("系统异常: traceId={}", traceId, ex);
        return ApiResponse.fail("SYSTEM_500", "系统异常，请稍后重试", Instant.now().toEpochMilli(), request.getRequestURI(), traceId);
    }
}
