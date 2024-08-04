package com.sky.service;

import com.sky.dto.SetmealDTO;

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
}
