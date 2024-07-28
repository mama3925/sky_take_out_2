package com.sky.mapper;

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
     * @author: xuwuyuan
     * @date: 2024/7/28 15:34
     * @desc: 为了配合Category业务实现类里的删除功能使用，确保无关联后才能删除
     * @param id
     * @return: java.lang.Integer
     */
    @Select("select count(*) from setmeal where category_id = #{id}")//这里犯错误了，我以为只有传入实体对象要加#{}，实际上就算是包装类等简单参数，也必须要#{}
    Integer countByCategoryId(Long id);

}
