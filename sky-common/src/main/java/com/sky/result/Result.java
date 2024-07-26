package com.sky.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 1;//将状态码改为1，表示结果成功
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<>();
        result.code = 1;
        result.data = object;//数据赋给内部类的data域
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.code = 0;//状态码改为0，表示请求失败
        result.msg = msg;//故障信息传递
        return result;
    }

}
