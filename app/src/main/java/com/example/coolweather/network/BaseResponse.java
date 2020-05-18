package com.example.coolweather.network;

public class BaseResponse<T> {

    /**
     * 错误码
     */
    private int code;
    /**
     * 额外信息
     */
    private String message;
    /**
     * 真正返回数据
     */
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccessful() {
        return code == 200;
    }
}
