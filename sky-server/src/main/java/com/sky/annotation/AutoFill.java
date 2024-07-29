package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: xuwuyuan
 * @desc: 用这个注解实现aop，采用方法为annotation加@autofill注解。
 * @create: 2024/7/29 10:23
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    OperationType value();//UPDATE与INSERT。用来标识在mapper类里面的annotation注解里，区分要不要写入两个create字段。
}
