package com.sky.exception;

/**
 * @author: xuwuyuan
 * @desc: 传入DTO对象缺少必须域
 * @create: 2024/7/28 10:28
 **/
public class FieldMissingException extends BaseException{

    public FieldMissingException(String msg) {super(msg);}

}
