package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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
}