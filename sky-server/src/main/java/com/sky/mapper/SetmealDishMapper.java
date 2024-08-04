package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
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
     * @param dishIDs
     * @author: xuwuyuan
     * @date: 2024/7/31 9:40
     * @desc: 通过dishid列表得到setmealid列表
     * @return: java.util.List<java.lang.Long>
     */
    List<Long> getSetmealIDsByDishIDs(List<Long> dishIDs);

    /**
     * @param setmealDishList
     * @author: xuwuyuan
     * @date: 2024/8/4 11:03
     * @desc: 批量插入关联表信息
     * @return: void
     */
    //这里不用加AutoFill注解，因为数据库中的套餐菜品关联表没有时间和用户字段
    void insertBatch(List<SetmealDish> setmealDishList);
}
