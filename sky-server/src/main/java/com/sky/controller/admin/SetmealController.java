package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @param setmealDTO
     * @author: xuwuyuan
     * @date: 2024/8/4 10:25
     * @desc: 新增套餐
     * @return: com.sky.result.Result
     */
    @PostMapping
    @ApiOperation("新建套餐")
    public Result insertSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐功能开始:{}", setmealDTO);
        setmealService.insertWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * @param setmealPageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/8/5 8:48
     * @desc: 分页查询
     * @return: com.sky.result.Result<com.sky.result.PageResult>
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询功能开始:{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * @param ids
     * @author: xuwuyuan
     * @date: 2024/8/5 9:00
     * @desc: 套餐批量删除
     * @return: com.sky.result.Result
     */
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    public Result delete(@RequestParam List<Long> ids) {//这里又漏了@RequestParam注解，对于列表参数，必须有。否则传不进去
        log.info("套餐批量删除功能开始:{}", ids);
        setmealService.deleteByIDs(ids);//输入套餐id列表，进行删除
        return Result.success();
    }

    /**
     * @param setmealDTO
     * @author: xuwuyuan
     * @date: 2024/8/5 11:13
     * @desc: 修改套餐
     * @return: com.sky.result.Result
     */
    @PutMapping
    @ApiOperation("修改套餐")
    public Result update(@RequestBody SetmealDTO setmealDTO) {//又漏了requestbody
        log.info("套餐修改功能开始:{}", setmealDTO);
        setmealService.updateWithDishes(setmealDTO);
        return Result.success();
    }

    /**
     * @param id
     * @author: xuwuyuan
     * @date: 2024/8/5 11:13
     * @desc: 根据id查询套餐
     * @return: com.sky.result.Result<com.sky.vo.SetmealVO>
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> findById(@PathVariable Long id) {
        log.info("套餐查询，id为:{}", id);
        SetmealVO setmealVO = setmealService.findByIdWithDish(id);
        return Result.success(setmealVO);
    }

    /**
     * @param status
     * @param id
     * @author: xuwuyuan
     * @date: 2024/8/6 8:47
     * @desc: 套餐启停
     * @return: com.sky.result.Result
     */
    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售、停售")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("套餐起售停手功能开始:{} {}", status, id);
        setmealService.startOrStop(status, id);
        return Result.success();
    }

}
