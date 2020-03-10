package com.aicc.security.uaa.vo;

import com.aicc.security.uaa.enums.ResponseEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * @description:  数据格式返回统一
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties
public class ResponseVO implements Serializable {

    private static final long serialVersionUID = -437839076132402939L;

    /**
     * 异常码
     */
    private Integer code;

    /**
     * 描述
     */
    private String message;

    /**
     * 数据
     */
    private Object data;

    public ResponseVO() {}

    public ResponseVO(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ResponseVO(Integer code, String msg, Object data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public ResponseVO(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public ResponseVO(ResponseEnum responseEnum, Object data) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
        this.data = data;
    }

    public static ResponseVO success(){
        return new ResponseVO(ResponseEnum.SUCCESS);
    }

    public static  ResponseVO success(Object data){
        return new ResponseVO(ResponseEnum.SUCCESS, data);
    }

    public static ResponseVO success(int code, String msg){
        return new ResponseVO(code, msg);
    }

    public static ResponseVO error(int code, String msg){
        return new ResponseVO(code, msg);
    }

    public static ResponseVO error(ResponseEnum responseEnum){
        return new ResponseVO(responseEnum);
    }

    public static ResponseVO error(ResponseEnum responseEnum, Object data){
        return new ResponseVO(responseEnum, data);
    }

    public static ResponseVO errorParams(String msg){
        return new ResponseVO(ResponseEnum.INCORRECT_PARAMS.getCode(), msg);
    }

    public static ResponseVO error(BindingResult result, MessageSource messageSource) {
        StringBuffer msg = new StringBuffer();
        //获取错误字段集合
        List<FieldError> fieldErrors = result.getFieldErrors();
        //获取本地locale,zh_CN
        Locale currentLocale = LocaleContextHolder.getLocale();
        //遍历错误字段获取错误消息
        for (FieldError fieldError : fieldErrors) {
            //获取错误信息
            String errorMessage = messageSource.getMessage(fieldError, currentLocale);
            //添加到错误消息集合内
            msg.append(fieldError.getField() + "：" + errorMessage + " , ");
        }
        return ResponseVO.error(ResponseEnum.INCORRECT_PARAMS, msg.toString());
    }

}
