package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * @author: xuwuyuan
 * @desc: 套餐业务接口
 * @create: 2024/8/4 10:04
 **/
public interface SetmealService {
    /**
     * @param setmealDTO
     * @author: xuwuyuan
     * @date: 2024/8/4 10:27
     * @desc: 插入带菜品的套餐对象
     * @return: void
     */
    void insertWithDish(SetmealDTO setmealDTO);

    /**
     * @param setmealPageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/8/4 12:29
     * @desc: 分页查询
     * @return: com.sky.result.PageResult
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * @param ids
     * @author: xuwuyuan
     * @date: 2024/8/5 9:02
     * @desc: 批量删除功能
     * @return: void
     */
    void deleteByIDs(List<Long> ids);

    /**
     * @param setmealDTO
     * @author: xuwuyuan
     * @date: 2024/8/5 9:45
     * @desc: 根据DTO对象来修改套餐，并且更改关联表，确保菜品变动都会实时反映在关联表里
     * @return: void
     */
    void updateWithDishes(SetmealDTO setmealDTO);

    /**
     * @param id
     * @author: xuwuyuan
     * @date: 2024/8/5 10:49
     * @desc: 通过id查询套餐，并返回vo对象
     * @return: com.sky.vo.SetmealVO
     */
    SetmealVO findByIdWithDish(Long id);

    /**
     * @param status
     * @param id
     * @author: xuwuyuan
     * @date: 2024/8/6 8:47
     * @desc: 套餐启停
     * @return: void
     */
    void startOrStop(Integer status, Long id);
}
