package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
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
 * @desc: 用户接口分类查询
 * @createDate: 2024/8/8 17:20
 **/
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "分类查询接口")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * @author: xuwuyuan
     * @date: 2024/8/9 13:09
     * @desc: 分类条件查询
     * @param type
     * @return: com.sky.result.Result<java.util.List<com.sky.entity.Category>>
     */
    @GetMapping("/list")
    @ApiOperation("条件查询")
    public Result<List<Category>> listCategory(Integer type) {
        log.info("c端分类条件查询:{}", type);
        return Result.success(categoryService.listByType(type));
    }
}
