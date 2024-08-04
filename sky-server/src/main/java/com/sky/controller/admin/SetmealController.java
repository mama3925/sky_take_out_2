package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: xuwuyuan
 * @desc: 套餐管理模块
 * @create: 2024/8/4 10:03
 **/
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐管理")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * @author: xuwuyuan
     * @date: 2024/8/4 10:25
     * @desc: 新增套餐
     * @param setmealDTO
     * @return: com.sky.result.Result
     */
    @PostMapping
    @ApiOperation("新增套餐")
    public Result insertSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐功能开始:{}", setmealDTO);
        setmealService.insertWithDish(setmealDTO);
        return Result.success();
    }

}
