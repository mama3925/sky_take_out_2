package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: xuwuyuan
 * @desc: 菜品控制类
 * @create: 2024/7/29 15:58
 **/
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * @param dishDTO
     * @author: xuwuyuan
     * @date: 2024/7/29 20:38
     * @desc: 新增菜品功能，这个功能必须要前后端联调，因为knife没办法实现图片上传。图片上传和新增菜品功能在后端设计里没集成在一起，但是在前端代码里集成了
     * @return: com.sky.result.Result
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result saveDish(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品功能开始:{}", dishDTO);
        dishService.save(dishDTO);
        return Result.success();
    }

    /**
     * @param dishPageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/7/30 9:56
     * @desc: 菜品分页查询。传入业务层时，可能需要规定默认参数
     * @return: com.sky.result.Result<com.sky.result.PageResult>
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询功能开始:{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * @param ids
     * @author: xuwuyuan
     * @date: 2024/7/30 21:58
     * @desc: 批量删除菜品
     * @return: com.sky.result.Result
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteBatch(@RequestParam List<Long> ids) {
        log.info("批量删除菜品:{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/31 12:15
     * @desc: 根据id查询菜品
     * @return: com.sky.result.Result<com.sky.vo.DishVO>
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品(回显)")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("菜品回显功能开始:{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * @param dishDTO
     * @author: xuwuyuan
     * @date: 2024/7/31 15:42
     * @desc: 修改菜品，同时修改口味表
     * @return: com.sky.result.Result
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品功能开始:{}", dishDTO);
        dishService.updateWithFlavor(dishDTO); //这里的方法最好由update重命名为updateWtihFlavor
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用停用菜品")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用停用菜品功能开始:{} {}", status, id);
        dishService.startOrStop(status, id);
        return Result.success();
    }
}
