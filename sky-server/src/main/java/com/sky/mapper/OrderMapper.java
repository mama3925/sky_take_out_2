package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author xuwuyuan
 * @version 1.0
 * @Desc 订单数据层
 * @createDate 2024/8/26 21:35
 **/
@Mapper
public interface OrderMapper {

    /**
     * @param order
     * @author xuwuyuan
     * @date 2024/8/31 10:03
     * @desc 插入订单
     * @return void
     **/
    void insert(Orders order);

    /**
     * @param orderNumber
     * @param userId
     * @author xuwuyuan
     * @date 2024/9/1 17:38
     * @desc 输入订单号和用户名，返回查询到的订单实体
     * @return com.sky.entity.Orders
     **/
    @Select("select * from orders where user_id = #{userId} and number = #{orderNumber}")
    Orders findByOrderNumberAndUserId(String orderNumber, Long userId);

    /**
     * @param order
     * @author xuwuyuan
     * @date 2024/9/1 17:46
     * @desc 更新订单的几个字段
     * @return void
     **/
    void update(Orders order);
}