package com;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-05-22 17:00
 **/
public class ResultInfo<T> implements Serializable {

    private static final long serialVersionUID = -3889951364431157984L;
    private boolean success = true;
    private T object;  //返回对象
    private String message; //提示信息
    private String code;
    private HashMap<String, Object> extParams = new HashMap();

    public HashMap<String, Object> getExtParams() {
        return extParams;
    }

    public ResultInfo() {
    }

    public ResultInfo(String message) {
        this.message = message;
    }

    public ResultInfo(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResultInfo(boolean success, T object) {
        this.object = object;
        this.success = success;
    }

    public ResultInfo(boolean success, T object, String message) {
        this.object = object;
        this.success = success;
        this.message = message;
    }

    public ResultInfo(boolean success, T object, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
        if (object != null) {
            this.object = object;
        }
    }

    public ResultInfo(boolean success) {
        this.success = success;
    }


    public static <T> ResultInfo<T> build(boolean success, T object) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setObject(object);
        resultInfo.setSuccess(success);

        return resultInfo;
    }

    public static <T> ResultInfo<T> build(boolean success) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setSuccess(success);
        return resultInfo;
    }

    public static <T> ResultInfo<T> buildSuccess(T object) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setObject(object);
        return resultInfo;
    }

    public static <T> ResultInfo<T> buildSuccess() {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.success = true;
        return resultInfo;
    }

    public static <T> ResultInfo<T> buildFail(String message) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setSuccess(false);
        resultInfo.setMessage(message);
        return resultInfo;
    }

    public static <T> ResultInfo<T> buildFail(String message, T object) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setSuccess(false);
        resultInfo.setObject(object);
        resultInfo.setMessage(message);
        return resultInfo;
    }

    public static <T> ResultInfo<List<T>> emptyList() {
        return ResultInfo.buildSuccess(Collections.EMPTY_LIST);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public void addExtParams(String key, Object value) {
        extParams.put(key, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResultInfo{");
        sb.append("success=").append(success);
        sb.append(", object=").append(object);
        sb.append(", code=").append(code);
        sb.append(", message='").append(message).append('\'');
        sb.append(", extParams=").append(extParams);
        sb.append('}');
        return sb.toString();
    }
}
