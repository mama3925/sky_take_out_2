package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * @author: xuwuyuan
 * @desc: 菜品持久层
 * @create: 2024/7/28 12:17
 **/
@Mapper
public interface DishMapper {

    /**
     * @author: xuwuyuan
     * @date: 2024/7/28 15:07
     * @desc: 为了配合Category业务实现类里的删除功能使用，确保无关联后才能删除
     * @param id
     * @return: java.lang.Integer
     */
    @Select("select count(*) from dish where category_id = #{id}")//这里犯错误了，我以为只有传入实体对象要加#{}，实际上就算是包装类等简单参数，也必须要#{}
    Integer countByCategoryId(Long id);

    /**
     * @author: xuwuyuan
     * @date: 2024/7/29 22:04
     * @desc: 插入单个菜品
     * @param dish
     * @return: void
     */
    @AutoFill(value = OperationType.INSERT) //这个漏了
    @Insert("insert into dish (name, category_id, price, image, description, status, " +
            "create_time, update_time, create_user, update_user) values " + //这里我就是漏了最后四个字段，所以未能实现公共字段填充，后来补上了
            "(#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{status}, " +
            "#{createTime}, #{updateTime}, #{createUser}, #{updateUser})")//这里也要对应补上
    @Options(useGeneratedKeys = true, keyProperty = "id")//这里和源码不同，我用自己习惯的方式实现，没必要加xml文件
    void insert(Dish dish);

}
