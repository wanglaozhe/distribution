package com.yuandong.vo;

/**
 * 统一封装API返回信息
 */
public class RestResult {
    //状态码
    private boolean success;
    //消息
    private String message;
    //额外的内容
    private Object data;

    public RestResult(){

    }

    public boolean isSuccess() {
		return success;
	}

    public RestResult setSuccess(boolean success) {
    	this.success = success;
        return this;
    }


	public String getMessage() {
        return message;
    }

    public RestResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public RestResult setData(Object data) {
        this.data = data;
        return this;
    }
}
