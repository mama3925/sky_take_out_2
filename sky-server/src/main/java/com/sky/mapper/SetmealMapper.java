package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author: xuwuyuan
 * @desc: 套餐持久层
 * @create: 2024/7/28 12:17
 **/
@Mapper
public interface SetmealMapper {

    /**
     * @param categoryId
     * @author: xuwuyuan
     * @date: 2024/7/28 15:34
     * @desc: 为了配合Category业务实现类里的删除功能使用，确保无关联后才能删除
     * @return: java.lang.Integer
     */
    @Select("select count(*) from setmeal where category_id = #{categoryId}")
//这里犯错误了，我以为只有传入实体对象要加#{}，实际上就算是包装类等简单参数，也必须要#{}
    Integer countByCategoryId(Long categoryId);

    /**
     * @param id
     * @author: xuwuyuan
     * @date: 2024/8/3 21:16
     * @desc: 通过id获取套餐
     * @return: com.sky.entity.Setmeal
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * @author: xuwuyuan
     * @date: 2024/8/3 21:51
     * @desc: 更新套餐表
     * @param setmeal
     * @return: void
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

}
