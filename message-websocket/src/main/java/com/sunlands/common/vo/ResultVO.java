package com.sunlands.common.vo;

import java.io.Serializable;

/**
 *
 * Created by tangwei on 2017/9/30.
 */
public class ResultVO implements Serializable {

    private static final long serialVersionUID = 6965671443768324878L;

    public static final Integer CODE_FAILURE = 0;

    public static final Integer CODE_SUCCESS = 1;

    /**
     * 返回编码，0-失败；1-成功.
     * 1000以内的编码保留，
     */
    private Integer flag;
    /**
     * 提示消息，如果返回码为失败，此处为失败的详细信息.
     */
    private String message;

    /**
     * 返回的具体数据对象.
     */
    private Object data;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResultVO setFailMessage(String message) {
        ResultVO result = new ResultVO();
        result.setFlag(CODE_FAILURE);
        result.setMessage(message);
        return result;
    }

    public static ResultVO setSuccessData(Object data) {
        ResultVO result = new ResultVO();
        result.setFlag(CODE_SUCCESS);
        result.setData(data);
        result.setMessage("");
        return result;
    }

    public static ResultVO setSuccessMessage(String message, Object data) {
        ResultVO result = new ResultVO();
        result.setFlag(CODE_SUCCESS);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
