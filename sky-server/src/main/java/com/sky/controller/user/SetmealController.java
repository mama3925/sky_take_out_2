package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: xuwuyuan
 * @desc: c端套餐浏览
 * @createDate: 2024/8/9 22:20
 **/
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "套餐浏览")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private DishService dishService;

    /**
     * @author: xuwuyuan
     * @date: 2024/8/9 22:25
     * @desc: 根据分类id查询套餐
     * @param categoryId
     * @return: java.util.List<com.sky.entity.Setmeal>
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询套餐")
    @Cacheable(value = "setmealCache", key = "#categoryId")
    public Result<List<Setmeal>> list(Long categoryId) {
        log.info("c端根据分类id查询套餐开始:{}", categoryId);
        List<Setmeal> list = setmealService.listByCategoryId(categoryId);
        return Result.success(list);
    }

    /**
     * @author: xuwuyuan
     * @date: 2024/8/9 22:53
     * @desc: 根据套餐id列出菜品
     * @param id
     * @return: com.sky.result.Result<java.util.List<com.sky.entity.Dish>>
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id列出菜品")
    public Result<List<DishItemVO>> listDishes(@PathVariable("id") Long id) {
        log.info("c端根据套餐id列出菜品:{}", id);
        List<DishItemVO> dishList = setmealService.listDishItemBySetmealId(id); //必须使用DishItemVO实体类，因为最终只涉及到setmeal_dish表的查询
        return Result.success(dishList);
    }

}
