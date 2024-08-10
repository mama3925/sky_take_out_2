package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: xuwuyuan
 * @desc: 分类管理控制类
 * @create: 2024/7/28 10:11
 **/
@RestController("adminCategoryController")
@Slf4j
@Api(tags = "分类相关接口")
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * @param categoryDTO
     * @author: xuwuyuan
     * @date: 2024/7/28 11:08
     * @desc: 新增分类功能
     * @return: com.sky.result.Result
     */
    @PostMapping
    @ApiOperation("新增分类")
    public Result addCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类功能开始:{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * @param categoryPageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/7/28 11:15
     * @desc: 分页查询所有分类，包括套餐分类和菜品分类
     * @return: com.sky.result.PageResult
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分页查询分类功能开始:{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * @param status
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/28 11:23
     * @desc:
     * @return: com.sky.result.Result
     */
    @PostMapping("status/{status}")
    @ApiOperation("启用禁用分类")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("分类启用禁用功能开始:{}, {}", status, id);
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/28 11:32
     * @desc: 根据id删除某个分类
     * @return: com.sky.result.Result
     */
    @DeleteMapping
    @ApiOperation("根据id删除分类")
    public Result deleteCategory(Long id) {
        log.info("分类删除功能开始:{}", id);
        categoryService.deleteById(id);//这个方法名适合用deleteById，而非delete。因为要突出靠id这个请求参数来删除分类
        return Result.success();
    }

    /**
     * @param categoryDTO
     * @author: xuwuyuan
     * @date: 2024/7/28 11:35
     * @desc: 分类修改功能
     * @return: com.sky.result.Result
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result modifyCategory(@RequestBody CategoryDTO categoryDTO) {//这里漏写了@RequestBody注解，导致前端json请求传不进去，值的批评！
        log.info("分类修改功能开始:{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * @author: xuwuyuan
     * @date: 2024/7/28 11:44
     * @desc: 根据int值type来查询对应分类，并返回一个String类型列表
     * @param type
     * @return: com.sky.result.Result<java.util.List<com.sky.entity.Category>>
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> listByType(Integer type) {
        log.info("根据类型查询分类:{}", type);
        List<Category> list = categoryService.listByType(type);
        return Result.success(list);
    }

}