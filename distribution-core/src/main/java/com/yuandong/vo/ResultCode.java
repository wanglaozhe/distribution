package com.yuandong.vo;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
    SUCCESS(true),//成功
    FAIL(false);//失败

    private boolean code;

    private ResultCode(boolean code) {
        this.code = code;
    }

    public boolean getCode() {
        return this.code;
    }
}
