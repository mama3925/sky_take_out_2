package com.sky.exception;

/**
 * @author: xuwuyuan
 * @desc: 这是我自己增加的异常类，指的是在数据库里没查到相关记录
 * @create: 2024/7/31 10:35
 **/
public class NoSuchRecordException extends BaseException {
    public NoSuchRecordException(String msg) {super(msg);}
}
