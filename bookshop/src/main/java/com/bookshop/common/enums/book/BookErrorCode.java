package com.bookshop.common.enums.book;

/**
 * 图书模块错误码枚举。
 * 作用：统一维护 book 领域的错误码和错误信息，避免魔法字符串散落在代码中。
 */
public enum BookErrorCode {
    BOOK_NOT_FOUND("BOOK_404", "图书不存在"),
    BOOK_DELETE_FAILED("BOOK_410", "删除失败，图书不存在");

    private final String code;
    private final String message;

    BookErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
