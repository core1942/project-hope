package com.example.projecthope.dto;

/**
 * create by WG on 2019/8/13 14:23
 *
 * @author WangGang
 */
public enum CodeEnum {
    /**
     * 成功-失败返回值
     */
    SUCCESS(0, "ok"),
    FAILURE(-1,"failure");

    private int code;
    private String message;

    CodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}