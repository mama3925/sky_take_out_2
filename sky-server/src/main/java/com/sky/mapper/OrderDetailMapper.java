package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xuwuyuan
 * @version 1.0
 * @Desc
 * @createDate 2024/8/31 10:18
 **/
@Mapper
public interface OrderDetailMapper {

    /**
     * @param orderDetailList
     * @author xuwuyuan
     * @date 2024/8/31 10:19
     * @desc 插入订单明细
     * @return void
     **/
    void insertBatch(List<OrderDetail> orderDetailList);
}
