package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation("新建套餐")
    public Result insertSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐功能开始:{}", setmealDTO);
        setmealService.insertWithDish(setmealDTO);
        return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询功能开始:{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }
}
