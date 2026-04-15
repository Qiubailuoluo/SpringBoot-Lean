package com.bookshop.common.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 统一接口返回结构。
 * 作用：
 * 1) 让所有接口返回格式一致，便于前后端联调；
 * 2) 后续可扩展错误码体系和全局异常处理。
 *
 * @param <T> data 字段的数据类型
 */
@Schema(description = "统一接口响应体")
public class ApiResponse<T> {

    @Schema(description = "业务码：成功通常为 SUCCESS，失败为业务错误码", example = "SUCCESS")
    private String code;
    @Schema(description = "是否成功", example = "true")
    private boolean success;
    @Schema(description = "响应消息", example = "查询成功")
    private String message;
    @Schema(description = "业务数据")
    private T data;
    @Schema(description = "毫秒时间戳（失败场景常用）", example = "1776232843177")
    private Long timestamp;
    @Schema(description = "请求路径（失败场景常用）", example = "/api/users/1")
    private String path;
    @Schema(description = "链路追踪ID（失败场景常用）", example = "e21656712e9c4f84ae2fdb3bfa35746b")
    private String traceId;

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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    /**
     * 构建成功响应（无 data）。
     *
     * @param message 成功提示文案
     * @param <T>     data 类型占位
     * @return 仅含成功标记与 message 的响应
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
     *
     * @param message 成功提示文案
     * @param data    业务数据
     * @param <T>     data 类型
     * @return 成功响应
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
     * 构建失败响应（无业务错误码，code 为 FAIL）。
     *
     * @param message 失败说明
     * @param <T>     data 类型占位
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
     *
     * @param code    如 VALIDATION_400、BOOK_404
     * @param message 可读说明
     * @param <T>     data 类型占位
     */
    public static <T> ApiResponse<T> fail(String code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    /**
     * 构建失败响应（附带时间戳、请求路径与 traceId，供全局异常处理器使用）。
     *
     * @param code      业务或系统错误码
     * @param message   可读说明
     * @param timestamp 毫秒时间戳
     * @param path      请求 URI
     * @param traceId   与 MDC / 响应头一致的链路 ID
     * @param <T>       data 类型占位
     */
    public static <T> ApiResponse<T> fail(String code, String message, Long timestamp, String path, String traceId) {
        ApiResponse<T> response = fail(code, message);
        response.setTimestamp(timestamp);
        response.setPath(path);
        response.setTraceId(traceId);
        return response;
    }
}
