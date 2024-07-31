package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: xuwuyuan
 * @desc:
 * @create: 2024/7/31 9:09
 **/
@Mapper
public interface SetmealDishMapper {
    /**
     * @author: xuwuyuan
     * @date: 2024/7/31 9:40
     * @desc: 通过dishid列表得到setmealid列表
     * @param dishIDs
     * @return: java.util.List<java.lang.Long>
     */
    List<Long> getSetmealIDsByDishIDs(List<Long> dishIDs);
}
