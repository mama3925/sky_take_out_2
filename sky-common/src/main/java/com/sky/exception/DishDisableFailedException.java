package com.sky.exception;

/**
 * @author: xuwuyuan
 * @desc: 包含该菜品套餐还在发售，所以无法停售
 * @create: 2024/8/3 20:56
 **/
public class DishDisableFailedException extends BaseException{

    public DishDisableFailedException(String msg) {super(msg);}

}
