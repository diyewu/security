package com.aicc.security.uaa.utils;


import com.aicc.security.uaa.domain.Result.ResponseResult;
import com.aicc.security.uaa.domain.Result.ResultCodeEnum;

/**
 * 通用Restful返回工具
 */
public class ResultUtils {

    private final static String SUCCESS = "success";

    private final static String FAIL = "fail";

    public static <T> ResponseResult<T> success() {
        return new ResponseResult<T>().setCode(ResultCodeEnum.SUCCESS).setMsg(SUCCESS);
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<T>().setCode(ResultCodeEnum.SUCCESS).setMsg(SUCCESS).setData(data);
    }

    public static ResponseResult success(String msg) {
        return new ResponseResult().setCode(ResultCodeEnum.SUCCESS).setMsg(msg);
    }

    public static <T> ResponseResult<T> success(ResultCodeEnum codeEnum, T data) {
        return new ResponseResult<T>().setCode(codeEnum.code).setMsg(codeEnum.msg).setData(data);
    }

    public static <T> ResponseResult<T> fail() {
        return new ResponseResult<T>().setCode(ResultCodeEnum.FAIL).setMsg(FAIL);
    }

    public static <T> ResponseResult<T> fail(ResultCodeEnum codeEnum) {
        return new ResponseResult<T>().setCode(codeEnum.code).setMsg(codeEnum.msg);
    }
}
