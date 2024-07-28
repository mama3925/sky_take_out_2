package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @author: xuwuyuan
 * @desc: 分类业务接口
 * @create: 2024/7/28 10:15
 **/
public interface CategoryService {

    /**
     * @param categoryDTO
     * @author: xuwuyuan
     * @date: 2024/7/28 11:49
     * @desc: 插入分类
     * @return: void
     */
    void save(CategoryDTO categoryDTO);

    /**
     * @param categoryPageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/7/28 11:49
     * @desc: 分类分页查询
     * @return: com.sky.result.PageResult
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/28 11:50
     * @desc: 删除分类
     * @return: void
     */
    void deleteById(Long id);//这个方法名适合用deleteById，而非delete。因为要突出靠id这个请求参数来删除分类

    /**
     * @param status
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/28 11:50
     * @desc: 启用禁用分类
     * @return: void
     */
    void startOrStop(Integer status, Long id);

    /**
     * @param categoryDTO
     * @author: xuwuyuan
     * @date: 2024/7/28 11:52
     * @desc: 修改分类
     * @return: void
     */
    void update(CategoryDTO categoryDTO);

    /**
     * @param type
     * @author: xuwuyuan
     * @date: 2024/7/28 11:53
     * @desc: 根据类型int获取分类名称
     * @return: java.util.List<com.sky.entity.Category>
     */
    List<Category> listByType(Integer type);

}
