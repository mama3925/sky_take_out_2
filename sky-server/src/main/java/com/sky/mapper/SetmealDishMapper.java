package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * @author: xuwuyuan
     * @date: 2024/8/5 9:31
     * @desc: 通过套餐id批量删除关联表记录
     * @param setmealId
     * @return: void
     */
    void deleteBatchBySetmealId(Long setmealId);

    /**
     * @author: xuwuyuan
     * @date: 2024/8/5 10:59
     * @desc: 通过套餐id查询关联列表
     * @param setmealId
     * @return: java.util.List<com.sky.entity.SetmealDish>
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> findBySetmealId(Long setmealId);
}
