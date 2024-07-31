package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @author: xuwuyuan
 * @desc: 菜品业务接口
 * @create: 2024/7/29 17:53
 **/
public interface DishService {

    /**
     * @param dishDTO
     * @author: xuwuyuan
     * @date: 2024/7/29 20:40
     * @desc: 新增菜品实现接口
     * @return: void
     */
    void save(DishDTO dishDTO);

    /**
     * @param dishPageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/7/30 10:17
     * @desc: 菜品分页查询
     * @return: com.sky.result.PageResult
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * @param ids
     * @author: xuwuyuan
     * @date: 2024/7/31 8:38
     * @desc: 批量删除菜品
     * @return: void
     */
    void deleteBatch(List<Long> ids);
}
