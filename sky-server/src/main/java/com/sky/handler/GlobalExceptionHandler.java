package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     *
     * @author: xuwuyuan
     * @date: 2024/7/26 19:20
     * @desc: 捕获sql异常，例如某个unique字段重复插入
     *
     * @param ex
     * @return: com.sky.result.Result
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String exceptionMessage = ex.getMessage();
        if (exceptionMessage.contains("Duplicate entry")) {
            String[] words = exceptionMessage.split(" ");
            String name = words[2];
            return Result.error(name + MessageConstant.ALREADY_EXISTS);
        } else return Result.error(MessageConstant.UNKNOWN_ERROR);
    }
}
