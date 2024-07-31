package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: xuwuyuan
 * @desc: 菜品口味数据操作层
 * @create: 2024/7/29 22:05
 **/
@Mapper
public interface DishFlavorMapper {

    /**
     * @param flavors
     * @author: xuwuyuan
     * @date: 2024/7/29 22:18
     * @desc: 批量插入口味
     * @return: void
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * @author: xuwuyuan
     * @date: 2024/7/31 9:55
     * @desc: 根据传入的菜品id列表来进行口味批量删除
     * @param dishIDs
     * @return: void
     */
    void deleteBatchByDishIDs(List<Long> dishIDs);
}
