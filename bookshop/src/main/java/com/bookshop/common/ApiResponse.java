package com.bookshop.common;

/**
 * 统一接口返回结构。
 * 作用：
 * 1) 让所有接口返回格式一致，便于前后端联调；
 * 2) 后续可扩展错误码体系和全局异常处理。
 *
 * @param <T> data 字段的数据类型
 */
public class ApiResponse<T> {

    private String code;
    private boolean success;
    private String message;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 构建成功响应（无 data）。
     */
    public static <T> ApiResponse<T> ok(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode("SUCCESS");
        response.setSuccess(true);
        response.setMessage(message);
        return response;
    }

    /**
     * 构建成功响应（包含 data）。
     */
    public static <T> ApiResponse<T> ok(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode("SUCCESS");
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    /**
     * 构建失败响应。
     */
    public static <T> ApiResponse<T> fail(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode("FAIL");
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    /**
     * 构建失败响应（带业务错误码）。
     */
    public static <T> ApiResponse<T> fail(String code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}
