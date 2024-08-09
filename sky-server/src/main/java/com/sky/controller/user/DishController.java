package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: xuwuyuan
 * @desc: c端菜品查询
 * @createDate: 2024/8/8 10:25
 **/
@RestController("userDishController")
@RequestMapping("/user/dish")
@Api(tags = "菜品浏览接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * @author: xuwuyuan
     * @date: 2024/8/8 10:33
     * @desc: 根据分类id查询菜品
     * @param categoryId
     * @return: com.sky.result.Result<java.util.List<com.sky.vo.DishVO>>
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id浏览菜品")
    public Result<List<DishVO>> listByCategoryId(Long categoryId) {
        log.info("c端查询菜品，分类id为:{}", categoryId);
        //这里需要返回一个DishVO对象，但是dishService里面实现的listDishByCategoryId只能实现返回Dish对象，所以只能在这里重构，新建一个dish对象封装数据
        Dish dish = Dish.builder()
                .status(StatusConstant.ENABLE)
                .categoryId(categoryId)
                .build();
        List<DishVO> dishVOList = dishService.listByCategoryIdWithFlavor(dish);
        return Result.success(dishVOList);//这里漏了加上返回对象
    }

}
