package com.bookshop.exception;

/**
 * 业务异常。
 * 作用：在 service 层抛出可预期的业务错误，并交给全局异常处理器统一转换为接口响应。
 */
public class BusinessException extends RuntimeException {

    private final String code;

    /**
     * @param code    与 {@link com.bookshop.common.response.ApiResponse} 约定的错误码字符串
     * @param message 对外或可读的异常说明
     */
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
