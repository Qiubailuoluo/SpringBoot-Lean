package com.bookshop.common.exception;

import com.bookshop.common.ApiResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器。
 * 作用：统一处理系统异常和业务异常，保证接口返回结构稳定。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常，返回业务错误码。
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException ex) {
        return ApiResponse.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理参数校验异常，返回首个校验失败信息。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidationException(MethodArgumentNotValidException ex) {
        String message = "参数校验失败";
        if (ex.getBindingResult().hasFieldErrors() && ex.getBindingResult().getFieldError() != null) {
            message = ex.getBindingResult().getFieldError().getDefaultMessage();
        }
        return ApiResponse.fail("VALIDATION_400", message);
    }

    /**
     * 兜底处理未知异常，避免堆栈信息直接暴露给客户端。
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception ex) {
        return ApiResponse.fail("SYSTEM_500", "系统异常，请稍后重试");
    }
}
