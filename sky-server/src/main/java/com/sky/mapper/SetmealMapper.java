package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

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
    //这里犯错误了，我以为只有传入实体对象要加#{}，实际上就算是包装类等简单参数，也必须要#{}
    @Select("select count(*) from setmeal where category_id = #{categoryId}")
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
     * @param setmeal
     * @author: xuwuyuan
     * @date: 2024/8/3 21:51
     * @desc: 更新套餐表
     * @return: void
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * @param setmeal
     * @author: xuwuyuan
     * @date: 2024/8/4 11:44
     * @desc:
     * @return: void
     */
    @AutoFill(OperationType.INSERT)
    //这里漏了两个部分，一个是generateKey, 另一个是keyProperty，没了这两个属性，那业务实现类里的id返回功能就失效了。可以参考dishmapper的代码，不用xml
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into setmeal (id, category_id, name, price, status, description, image, " +
            "create_time, update_time, create_user, update_user) values " +
            "(#{id}, #{categoryId}, #{name}, #{price}, #{status}, #{description}, #{image}, " +
            "#{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Setmeal setmeal);

    /**
     * @param setmealPageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/8/4 12:35
     * @desc: 分页查询
     * @return: com.github.pagehelper.Page<com.sky.entity.Setmeal>
     */
    Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * @param setmealId
     * @author: xuwuyuan
     * @date: 2024/8/5 9:36
     * @desc: 根据id删除对应套餐
     * @return: void
     */
    @Delete("delete from setmeal where id = #{setmealId}")
    void deleteById(Long setmealId);

//    *
//     * @author: xuwuyuan
//     * @date: 2024/8/9 22:37
//     * @desc: 根据传入实体删除对应套餐记录
//     * @param setmeal
//     * @return: java.util.List<com.sky.entity.Setmeal>
//
//    List<Setmeal> list(Setmeal setmeal);
//
//    *
//     * @author: xuwuyuan
//     * @date: 2024/8/10 9:51
//     * @desc: 连接套餐表和关联表，之后分别取出四个属性。
//     * @param setmealId
//     * @return:
//
//    @Select("select sd.name, sd.copies, d.image, d.description from dish d right join setmeal_dish sd on d.id = sd.dish_id " +
//            "where sd.setmeal_id = #{setmealId}")
//    List<DishItemVO> getDishItemsBySetmealId(Long setmealId);

    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemsBySetmealId(Long setmealId);
}
