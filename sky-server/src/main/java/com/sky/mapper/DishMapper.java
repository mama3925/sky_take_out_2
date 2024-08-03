package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: xuwuyuan
 * @desc: 菜品持久层
 * @create: 2024/7/28 12:17
 **/
@Mapper
public interface DishMapper {

    /**
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/28 15:07
     * @desc: 为了配合Category业务实现类里的删除功能使用，确保无关联后才能删除
     * @return: java.lang.Integer
     */
    @Select("select count(*) from dish where category_id = #{id}")
//这里犯错误了，我以为只有传入实体对象要加#{}，实际上就算是包装类等简单参数，也必须要#{}
    Integer countByCategoryId(Long id);

    /**
     * @param dish
     * @author: xuwuyuan
     * @date: 2024/7/29 22:04
     * @desc: 插入单个菜品
     * @return: void
     */
    @AutoFill(value = OperationType.INSERT) //这个漏了
    @Insert("insert into dish (name, category_id, price, image, description, status, " +
            "create_time, update_time, create_user, update_user) values " + //这里我就是漏了最后四个字段，所以未能实现公共字段填充，后来补上了
            "(#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{status}, " +
            "#{createTime}, #{updateTime}, #{createUser}, #{updateUser})")//这里也要对应补上
    @Options(useGeneratedKeys = true, keyProperty = "id")
//这里和源码不同，我用自己习惯的方式实现，没必要加xml文件
    void insert(Dish dish);

    /**
     * @param dishPageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/7/30 11:11
     * @desc: 菜品分类查询
     * @return: com.github.pagehelper.Page<com.sky.vo.DishVO>
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * @param ids
     * @author: xuwuyuan
     * @date: 2024/7/31 9:51
     * @desc: 通过传入id列表批量删除菜品
     * @return: void
     */
    void deleteBatch(List<Long> ids);

    /**
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/31 10:09
     * @desc: 通过id查询菜品表
     * @return: com.sky.entity.Dish
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * @author: xuwuyuan
     * @date: 2024/8/3 16:42
     * @desc: 修改菜品表
     * @param dish
     * @return: void
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);
}
