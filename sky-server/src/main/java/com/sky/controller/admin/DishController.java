package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
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
     * @author: xuwuyuan
     * @date: 2024/7/29 20:38
     * @desc: 新增菜品功能，这个功能必须要前后端联调，因为knife没办法实现图片上传。图片上传和新增菜品功能在后端设计里没集成在一起，但是在前端代码里集成了
     * @param dishDTO
     * @return: com.sky.result.Result
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result saveDish(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品功能开始:{}", dishDTO);
        dishService.save(dishDTO);
        return Result.success();
    }


}
