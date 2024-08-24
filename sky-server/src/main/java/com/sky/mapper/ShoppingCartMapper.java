package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.ShoppingCart;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author xuwuyuan
 * @version 1.0
 * @Desc
 * @createDate 2024/8/23 21:59
 **/
@Mapper
public interface ShoppingCartMapper {

    /**
     * @param shoppingCart
     * @author xuwuyuan
     * @date 2024/8/24 14:36
     * @desc 插入购物车商品项
     * @return void
     **/
    @Insert("insert into shopping_cart (name, user_id, dish_id, setmeal_id, dish_flavor, " +
            "number, amount, image, create_time) values (#{name}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, " +
            "#{number}, #{amount}, #{image}, #{createTime})")
    // 这里不能自作聪明，使用自制的autofill注解，因为该表没有createUser字段，所以会报错
    void save(ShoppingCart shoppingCart);

    /**
     * @param shoppingCart
     * @author xuwuyuan
     * @date 2024/8/24 14:37
     * @desc 通过购物车id来更新同一个商品的数量
     * @return void
     **/
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    // 这里不能自作聪明，使用自制的autofill注解，因为该表没有createUser字段，所以会报错
    void updateNumber(ShoppingCart shoppingCart);

    /**
     * @param shoppingCart
     * @author xuwuyuan
     * @date 2024/8/24 14:45
     * @desc 根据传入购物车实体，查询出一个符合要求的购物车列表
     * @return java.util.List<com.sky.entity.ShoppingCart>
     **/
    List<ShoppingCart> list(ShoppingCart shoppingCart);
}
